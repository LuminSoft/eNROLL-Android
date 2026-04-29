package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.activityViewModels
import com.innovatrics.dot.nfc.NfcTravelDocumentReader
import com.innovatrics.dot.nfc.reader.NfcTravelDocumentReaderResult
import com.innovatrics.dot.nfc.reader.ui.NfcTravelDocumentReaderFragment

class DefaultNfcTravelDocumentReaderFragment : NfcTravelDocumentReaderFragment() {

    private val nfcReadingViewModel: NfcReadingViewModel by activityViewModels { NfcReadingViewModelFactory(requireActivity().application) }

    /**
     * Reader lifecycle phase as reported by Innovatrics callbacks.
     *  IDLE      – before first event, or after terminal success/failure reset.
     *  SEARCHING – library is waiting for an NFC tag (onSearchingStarted).
     *  READING   – tag found, library is attempting BAC/PACE and reading DGs
     *              (onReadingStarted).
     * Transitions SEARCHING→READING count as *distinct* chip contacts and are
     * the only reliable way to tell wrong-passport apart from hold-and-move.
     */
    private enum class ReaderPhase { IDLE, SEARCHING, READING }

    private var phase: ReaderPhase = ReaderPhase.IDLE

    /**
     * Number of distinct chip contacts observed in the current scan session.
     *
     * Incremented on every SEARCHING → READING transition (each time the
     * library moves from "looking for a tag" into "talking to a tag"). Extra
     * onReadingStarted callbacks that arrive while we are already in READING
     * (e.g. per-file callbacks) do NOT inflate the counter.
     *
     * Interpretation:
     *  - 1 contact total   → the tag was seen exactly once; a subsequent
     *    search restart means the tag was physically removed. This is the
     *    hold-and-move pattern → classify as retryable NFCConnectionError.
     *  - 2+ contacts total → the tag keeps reappearing at the antenna while
     *    the session repeatedly fails. The chip is physically present but
     *    access control is failing every time → wrong-passport pattern →
     *    terminal NFCInvalidMRZKey (10212), no retry, first attempt.
     */
    private var chipContactAttempts: Int = 0

    /**
     * Number of times onSearchingStarted fired after the first chip contact.
     * Kept for diagnostic logging only; no longer drives any terminal verdict
     * because it cannot distinguish "chip physically removed" from "chip
     * disconnected by BAC failure".
     */
    private var searchRestartCount: Int = 0

    /**
     * True once onReadingStarted has been observed with `numberOfElementaryFiles > 0`.
     *
     * The int passed to onReadingStarted is NOT a plain file count – per the
     * Innovatrics dot-nfc 9.0.2 library internals it is a sentinel whose value
     * reflects the current reading phase:
     *   0   → AccessEstablishmentStarted    (BAC/PACE about to start, not yet OK)
     *   100 → DataAuthenticationStarted     (BAC already succeeded)
     *   >0  → ElementaryFilesReadingProgressUpdated (BAC already succeeded)
     *
     * So any value > 0 is conclusive proof that access control (the MRZ-derived
     * key) matched the chip in this scan session. Once the flag latches to
     * true, all subsequent chip re-contacts are treated as retries of a
     * CORRECT passport (hold-and-move), never as wrong-passport.
     */
    private var bacSucceededAtLeastOnce: Boolean = false

    /**
     * Latched once we report a terminal verdict via [NfcReadingViewModel.setNfcError].
     *
     * The Innovatrics NfcTravelDocumentReaderFragment is NOT stopped when the
     * viewmodel flips into an error state – it stays subscribed to NFC reader
     * mode and keeps firing callbacks until the user dismisses the error dialog
     * and the activity is finished. Without this latch, a user tapping the
     * passport a second time while the dialog is on screen would re-enter our
     * callbacks, increment counters and potentially fire a redundant terminal
     * verdict or a spurious retryable failure. Once terminal is reported we
     * swallow every subsequent callback.
     */
    private var terminalReported: Boolean = false

    /**
     * Wall-clock time (SystemClock.elapsedRealtime) of the most recent
     * SEARCHING → READING transition (chip contact #1). Used by the
     * wrong-passport heuristic in [onReadingStarted] to distinguish the
     * two patterns that both produce `chipContactAttempts >= 2`:
     *
     *  - Wrong passport: the chip never physically leaves the antenna; the
     *    Innovatrics library detects the BAC failure internally and re-enters
     *    search in milliseconds, then fires onReadingStarted again very
     *    shortly after. The full #1 → #2 cycle is well under
     *    [MIN_HUMAN_REMOVAL_MS] in observed runs (~40 ms in the field log).
     *  - Hold-and-move: the user physically lifts the phone, the library
     *    fires onSearchingStarted as the tag goes out of range, the user
     *    repositions and re-taps. Even a fast user takes several hundred
     *    milliseconds for this; observed minimum in real testing is well
     *    above [MIN_HUMAN_REMOVAL_MS].
     *
     * 0 means "no contact #1 timestamp yet" (also reset by [resetSessionCounters]).
     */
    private var firstChipContactAtMs: Long = 0L

    override fun provideConfiguration(): Configuration {
        return nfcReadingViewModel.state.value.configuration!!
    }

    override fun onSearchingStarted() {
        super.onSearchingStarted()
        if (terminalReported) return

        val previousPhase = phase
        phase = ReaderPhase.SEARCHING

        if (chipContactAttempts == 0) {
            Log.d("NfcReading", "onSearchingStarted – initial search (no chip contact yet)")
            return
        }

        // A search restart after the chip has already been contacted can mean
        // either:
        //   (a) the chip was physically removed (hold-and-move)
        //   (b) the chip disconnected due to BAC/PACE failure but is still on
        //       the antenna and will re-trigger onReadingStarted shortly
        //       (wrong-passport pattern)
        // We no longer try to guess from this event alone – the definitive
        // signal is whether onReadingStarted fires again (see that callback).
        searchRestartCount++
        Log.d(
            "NfcReading",
            "onSearchingStarted – search restart #$searchRestartCount " +
                "(chipContactAttempts=$chipContactAttempts, previousPhase=$previousPhase, " +
                "bacOk=$bacSucceededAtLeastOnce)",
        )
    }

    override fun onReadingStarted(numberOfElementaryFiles: Int) {
        super.onReadingStarted(numberOfElementaryFiles)
        if (terminalReported) return

        // Latch the BAC-succeeded flag as soon as we observe any value > 0 –
        // see the field KDoc for why this is the correct signal.
        val bacJustSucceededThisCallback = numberOfElementaryFiles > 0 && !bacSucceededAtLeastOnce
        if (numberOfElementaryFiles > 0) {
            bacSucceededAtLeastOnce = true
        }

        val enteringFromSearch = phase != ReaderPhase.READING
        phase = ReaderPhase.READING

        if (!enteringFromSearch) {
            // Already in READING – this is a secondary callback for the same
            // contact (e.g. BAC→DataAuth→file-progress transitions, or internal
            // progress updates). Do NOT count as a new physical chip contact.
            Log.d(
                "NfcReading",
                "onReadingStarted – secondary callback while already READING " +
                    "(files=$numberOfElementaryFiles, bacOk=$bacSucceededAtLeastOnce)",
            )
            if (bacJustSucceededThisCallback) {
                Log.i(
                    "NfcReading",
                    "BAC/access-control succeeded – chip is compatible with the provided MRZ key",
                )
            }
            return
        }

        chipContactAttempts++
        val nowMs = SystemClock.elapsedRealtime()
        val msSinceFirstContact = if (firstChipContactAtMs == 0L) 0L
            else nowMs - firstChipContactAtMs
        if (chipContactAttempts == 1) {
            firstChipContactAtMs = nowMs
        }
        Log.d(
            "NfcReading",
            "onReadingStarted – chip contact #$chipContactAttempts " +
                "(files=$numberOfElementaryFiles, bacOk=$bacSucceededAtLeastOnce, " +
                "msSinceFirstContact=$msSinceFirstContact)",
        )
        if (bacJustSucceededThisCallback) {
            Log.i(
                "NfcReading",
                "BAC/access-control succeeded – chip is compatible with the provided MRZ key",
            )
        }

        // Wrong-passport detection – fires ONLY when ALL THREE conditions hold:
        //  1. The chip has physically re-appeared at the antenna
        //     (chipContactAttempts >= 2), AND
        //  2. BAC has NEVER succeeded in this session
        //     (!bacSucceededAtLeastOnce), AND
        //  3. The contact-#1 → contact-#2 cycle completed faster than a
        //     human can physically lift the phone and re-tap
        //     (msSinceFirstContact < MIN_HUMAN_REMOVAL_MS).
        //
        // Why all three are required:
        //  * (1) alone fires false positives whenever the user moves the phone
        //    mid-read on a CORRECT passport.
        //  * (1)+(2) (the previous gate) still false-fires on hold-and-move
        //    BEFORE BAC ever succeeded, because the JMRTD/Innovatrics readers
        //    surface BAC success only after the first elementary file is read –
        //    if the user moves during PACE/BAC there is no `bacSucceededAtLeastOnce`
        //    latch yet, and #2 looks identical to a wrong-passport re-tap from
        //    just these two flags.
        //  * (3) is the deciding signal. The Innovatrics library's internal
        //    BAC retry on a SAME chip that doesn't open with the supplied
        //    MRZ key is a millisecond-scale loop – the search restart in field
        //    logs lands ~40 ms after the failing onReadingStarted, and
        //    onReadingStarted #2 lands ~40 ms after that. By contrast, the
        //    fastest human "move and re-tap" cycle is several hundred
        //    milliseconds (>1 s in typical use). MIN_HUMAN_REMOVAL_MS sits
        //    comfortably between the two regimes.
        //
        // Net effect on user-visible behaviour:
        //  * Wrong passport (chip stays at antenna, BAC fails internally):
        //    terminal NFCInvalidMRZKey (10212) on contact #2 – first user
        //    attempt, no retry. (Production parity preserved.)
        //  * Hold-and-move on a CORRECT passport: heuristic skipped,
        //    `chipContactAttempts` keeps growing as the user re-taps; the
        //    Innovatrics library will eventually surface a real failure
        //    through onFailed, which the viewmodel classifier handles with
        //    its own MAX_RETRYABLE_FAILURES cap. (Bug fix.)
        if (chipContactAttempts >= 2 &&
            !bacSucceededAtLeastOnce &&
            msSinceFirstContact in 1 until MIN_HUMAN_REMOVAL_MS
        ) {
            Log.w(
                "NfcReading",
                "Chip re-contacted $chipContactAttempts× with BAC never succeeding " +
                    "in ${msSinceFirstContact}ms (< ${MIN_HUMAN_REMOVAL_MS}ms human-removal " +
                    "threshold) – MRZ/NFC mismatch detected, reporting terminal " +
                    "NFCInvalidMRZKey (10212)",
            )
            terminalReported = true
            resetSessionCounters()
            nfcReadingViewModel.setNfcError(Exception("access control failed"))
            return
        }
        if (chipContactAttempts >= 2 && !bacSucceededAtLeastOnce) {
            Log.d(
                "NfcReading",
                "Chip re-contacted $chipContactAttempts× after ${msSinceFirstContact}ms " +
                    "(>= ${MIN_HUMAN_REMOVAL_MS}ms) – treating as hold-and-move retry, " +
                    "letting the reader run again",
            )
        }

        // Hard cap on chip-contact retries within a single scan session.
        //
        // Two distinct silent-loop patterns require this cap, because in both
        // the Innovatrics library (dot-nfc 9.0.2) keeps ping-ponging
        // onSearchingStarted ↔ onReadingStarted forever and never calls
        // onSucceeded or onFailed:
        //
        //  (A) BAC never succeeded (!bacSucceededAtLeastOnce):
        //      Wrong passport tapped slowly, demagnetised chip, faulty
        //      antenna alignment, etc. The library retries BAC every time the
        //      chip reappears. The user sees no feedback until the library's
        //      own 60-second scan timeout, then gets a generic
        //      NFCTimeOutError (10211) that doesn't explain the real cause.
        //      Field log 2026-04-29 16:24:13–16:25:05.
        //
        //  (B) BAC succeeded but the LDS read never completes
        //      (bacSucceededAtLeastOnce). Hold-and-move on a correct passport
        //      where the chip is lost mid-read every time, or the chip data
        //      is corrupt and triggers an internal exception that the library
        //      swallows. The earlier KDoc here assumed the library would
        //      surface the error via onFailed eventually; field log
        //      2026-04-29 16:40:13–16:41:47 disproves that – six chip
        //      contacts over 90 s, BAC succeeded twice, no onFailed ever
        //      fired, no dialog ever appeared, user gave up manually.
        //
        // Both patterns are bounded by the same MAX_HOLD_AND_MOVE_RETRIES
        // budget. The terminal exception message routes the failure to the
        // appropriate NfcErrorCode bucket via classifyNfcError's fallback:
        //
        //  - (A) "hold-and-move retry budget exhausted" → NFCGeneralError
        //    (10209). The user sees the generic "couldn't read passport"
        //    dialog – correct, since at this point we cannot tell wrong
        //    passport from antenna failure with high confidence.
        //  - (B) "post-BAC retry budget exhausted" → NFCGeneralError (10209).
        //    Same dialog – BAC matched the MRZ so the passport is the right
        //    one, but the chip read could not complete.
        //
        // The cap value matches the dev-lumin JMRTD branch's
        // MAX_RETRYABLE_FAILURES = 4, so user-visible retry behaviour is
        // consistent across the two NFC backends.
        if (chipContactAttempts > MAX_HOLD_AND_MOVE_RETRIES) {
            val terminalMessage = if (bacSucceededAtLeastOnce) {
                "post-BAC retry budget exhausted"
            } else {
                "hold-and-move retry budget exhausted"
            }
            Log.w(
                "NfcReading",
                "Chip-contact retry budget exhausted (chipContactAttempts=" +
                    "$chipContactAttempts > $MAX_HOLD_AND_MOVE_RETRIES retries, " +
                    "bacOk=$bacSucceededAtLeastOnce) – reporting terminal " +
                    "NFCGeneralError (10209) to avoid the Innovatrics library's " +
                    "silent ping-pong loop. terminalMessage=$terminalMessage",
            )
            terminalReported = true
            resetSessionCounters()
            nfcReadingViewModel.setNfcError(Exception(terminalMessage))
            return
        }
    }

    override fun onSucceeded(result: NfcTravelDocumentReaderResult) {
        if (terminalReported) {
            Log.d("NfcReading", "onSucceeded after terminal already reported – ignoring")
            return
        }
        Log.d("NfcReading", "NFC reading succeeded")
        resetSessionCounters()
        bacSucceededAtLeastOnce = false
        nfcReadingViewModel.process(result)
    }

    override fun onFailed(exception: Exception) {
        if (terminalReported) {
            Log.d(
                "NfcReading",
                "onFailed after terminal already reported – ignoring: ${exception.javaClass.simpleName}",
            )
            return
        }

        val chain = generateSequence(exception as Throwable) { it.cause }
            .joinToString(" → ") { "${it.javaClass.simpleName}(${it.message})" }
        Log.e(
            "NfcReading",
            "NFC reading failed – chipContactAttempts=$chipContactAttempts " +
                "searchRestartCount=$searchRestartCount bacOk=$bacSucceededAtLeastOnce chain: $chain",
            exception,
        )

        // Log DebugInfo when available on ReadException (BAC trace, access-control result)
        val readEx = generateSequence(exception as Throwable) { it.cause }
            .filterIsInstance<NfcTravelDocumentReader.ReadException>()
            .firstOrNull()
        if (readEx != null) {
            try {
                val info = readEx.debugInfo
                Log.d(
                    "NfcReading",
                    "DebugInfo: bacTrace=${info?.bacExceptionStackTrace}" +
                        " accessControlResult=${info?.accessControlResult}" +
                        " bacResult=${info?.bacProtocolResult}" +
                        " paceResult=${info?.paceProtocolResult}",
                )
            } catch (e: Exception) {
                Log.w("NfcReading", "Could not read DebugInfo", e)
            }
        }

        // If BAC succeeded even once in this session, this can NEVER be a
        // wrong-passport failure – force the viewmodel classifier onto its
        // retryable branch by hiding the real chipContactAttempts value.
        // Without this override, two chip contacts + NotConnectedException
        // (e.g. hold-and-move + re-tap on a valid passport) would be
        // misclassified as NFCInvalidMRZKey by NfcReadingViewModel.classifyNfcError.
        val contactsForClassifier = if (bacSucceededAtLeastOnce) 1 else chipContactAttempts
        val bacWasOk = bacSucceededAtLeastOnce
        resetSessionCounters()
        bacSucceededAtLeastOnce = false
        if (bacWasOk) {
            Log.d(
                "NfcReading",
                "Suppressing chipContactAttempts for classifier (BAC had succeeded – retryable)",
            )
        }
        nfcReadingViewModel.reportNfcAttemptFailure(exception, contactsForClassifier)
    }

    private fun resetSessionCounters() {
        phase = ReaderPhase.IDLE
        chipContactAttempts = 0
        searchRestartCount = 0
        firstChipContactAtMs = 0L
        // NOTE: terminalReported is intentionally NOT reset here. Once a
        // terminal verdict has been reported the Innovatrics reader is still
        // alive under the error dialog and must continue to be ignored until
        // the fragment is destroyed.
    }

    companion object {
        /**
         * Minimum elapsed time (ms) between chip contact #1 and chip contact #2
         * that we will accept as evidence of a HUMAN move-and-re-tap cycle on a
         * correct passport, rather than the Innovatrics library's internal
         * BAC-retry loop on a wrong passport.
         *
         * Empirical anchors (Samsung S22 / dot-nfc 9.0.2, see field log
         * 2026-04-29 15:33–34 for the original wrong-passport regression):
         *  * Library-internal BAC retry cycle: ~40 ms
         *    (onReadingStarted → onSearchingStarted ≈ 37 ms,
         *     onSearchingStarted → next onReadingStarted ≈ 1.2 s in that
         *     specific log because the user was holding the device away – the
         *     pure library cycle is ~40 ms when the chip stays at the
         *     antenna).
         *  * Fastest observed human re-tap on a correct passport: ~700 ms.
         *
         * 500 ms gives a comfortable margin on both sides. If a future
         * device/library version closes this gap, raise the constant; do NOT
         * lower it – false positives here are user-visible terminal failures.
         */
        private const val MIN_HUMAN_REMOVAL_MS: Long = 500L

        /**
         * Maximum number of hold-and-move retries the Innovatrics path will
         * accept while BAC has NEVER succeeded in the current session.
         *
         * Mirrors `MAX_RETRYABLE_FAILURES = 4` in the dev-lumin branch's
         * `NfcReadingViewModel.classifyNfcError`, but with an unavoidable
         * one-event offset because the two paths surface failures
         * differently:
         *  - JMRTD/dev-lumin: per-attempt `onFailed` allows the classifier
         *    to count failures and upgrade the *next* attempt to terminal
         *    on its failure event.
         *  - Innovatrics: there is no per-attempt failure event, only
         *    `onReadingStarted` for the next contact. Terminal therefore
         *    has to fire at the START of the next contact, before that
         *    contact gets a chance to run.
         *
         * `chipContactAttempts > MAX_HOLD_AND_MOVE_RETRIES = 4` triggers
         * the terminal:
         *  - Contact #1 (initial) and contacts #2..#4 (three real retries
         *    where BAC actually gets to run) are allowed.
         *  - Contact #5 enters `onReadingStarted` and is immediately
         *    upgraded to terminal NFCInvalidMRZKey (10212).
         *
         * This is a deliberate trade-off vs the field log
         * (2026-04-29 16:09:00–16:09:54) where the user re-tapped four
         * times and then waited ~35 s for the library's own 60 s timeout
         * to fire NFCTimeOutError (10211). Sacrificing one nominal retry
         * is preferable to that long silent wait.
         */
        private const val MAX_HOLD_AND_MOVE_RETRIES: Int = 4
    }
}
