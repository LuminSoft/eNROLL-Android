package com.luminsoft.enroll_sdk.innovitices.nfcreading

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
        Log.d(
            "NfcReading",
            "onReadingStarted – chip contact #$chipContactAttempts " +
                "(files=$numberOfElementaryFiles, bacOk=$bacSucceededAtLeastOnce)",
        )
        if (bacJustSucceededThisCallback) {
            Log.i(
                "NfcReading",
                "BAC/access-control succeeded – chip is compatible with the provided MRZ key",
            )
        }

        // Wrong-passport detection – fires ONLY when BOTH conditions hold:
        //  1. The chip has physically re-appeared at the antenna
        //     (chipContactAttempts >= 2), AND
        //  2. BAC has NEVER succeeded in this session (bacSucceededAtLeastOnce
        //     is false).
        //
        // Condition 1 alone is not enough: on a CORRECT passport the user may
        // tap, move away mid-read (triggering onSearchingStarted restart and
        // then another onReadingStarted when re-tapping) – firing terminal
        // 10212 there would be a false positive. The extra !bacSucceededAtLeastOnce
        // gate ensures we only call it a mismatch when the key has demonstrably
        // never opened the chip in this session.
        if (chipContactAttempts >= 2 && !bacSucceededAtLeastOnce) {
            Log.w(
                "NfcReading",
                "Chip re-contacted $chipContactAttempts× with BAC never succeeding " +
                    "– MRZ/NFC mismatch detected, reporting terminal NFCInvalidMRZKey (10212)",
            )
            terminalReported = true
            resetSessionCounters()
            nfcReadingViewModel.setNfcError(Exception("access control failed"))
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
        // NOTE: terminalReported is intentionally NOT reset here. Once a
        // terminal verdict has been reported the Innovatrics reader is still
        // alive under the error dialog and must continue to be ignored until
        // the fragment is destroyed.
    }
}
