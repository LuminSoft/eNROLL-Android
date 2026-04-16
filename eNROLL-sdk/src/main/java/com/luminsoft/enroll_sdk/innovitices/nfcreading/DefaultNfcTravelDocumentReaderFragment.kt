package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.util.Log
import androidx.fragment.app.activityViewModels
import com.innovatrics.dot.nfc.NfcTravelDocumentReader
import com.innovatrics.dot.nfc.reader.NfcTravelDocumentReaderResult
import com.innovatrics.dot.nfc.reader.ui.NfcTravelDocumentReaderFragment

class DefaultNfcTravelDocumentReaderFragment : NfcTravelDocumentReaderFragment() {

    private val nfcReadingViewModel: NfcReadingViewModel by activityViewModels { NfcReadingViewModelFactory(requireActivity().application) }

    /**
     * True once onReadingStarted() fires (chip physically contacted).
     * onReadingStarted() fires per-elementary-file so we use a boolean
     * rather than a counter to avoid inflation.
     */
    private var hasContactedChip = false

    /**
     * Number of times onSearchingStarted() fired AFTER the chip was contacted.
     * Each such event means the library restarted the NFC tag search —
     * the chip disconnected (BAC/PACE failure or tag lost).
     * Two or more rapid restarts indicate wrong-passport / access-control failure.
     */
    private var searchRestartCount = 0

    override fun provideConfiguration(): Configuration {
        return nfcReadingViewModel.state.value.configuration!!
    }

    override fun onSearchingStarted() {
        super.onSearchingStarted()

        if (!hasContactedChip) {
            Log.d("NfcReading", "onSearchingStarted – initial search (no chip contact yet)")
            return
        }

        searchRestartCount++
        Log.d("NfcReading", "onSearchingStarted – search restart #$searchRestartCount after chip contact")

        // The Innovatrics library silently retries when it gets a
        // NotConnectedException (chip disconnects on BAC failure = wrong passport).
        // It restarts the NFC tag receiver and calls onSearchingStarted() again,
        // but NEVER calls onFailed(). The only visible outcome without this
        // intercept would be our 60-second timeout.
        //
        // Two restarts after chip contact = the chip is physically present but
        // access control keeps failing at the transport level → wrong passport.
        if (searchRestartCount >= 2) {
            Log.w(
                "NfcReading",
                "Innovatrics internal retry #$searchRestartCount after chip contact " +
                    "– reporting terminal access-control failure immediately",
            )
            hasContactedChip = false
            searchRestartCount = 0
            nfcReadingViewModel.setNfcError(Exception("access control failed"))
        }
    }

    override fun onReadingStarted(numberOfElementaryFiles: Int) {
        super.onReadingStarted(numberOfElementaryFiles)
        if (!hasContactedChip) {
            hasContactedChip = true
            Log.d("NfcReading", "onReadingStarted – chip contacted, files=$numberOfElementaryFiles")
        }
    }

    override fun onSucceeded(result: NfcTravelDocumentReaderResult) {
        Log.d("NfcReading", "NFC reading succeeded")
        hasContactedChip = false
        searchRestartCount = 0
        nfcReadingViewModel.process(result)
    }

    override fun onFailed(exception: Exception) {
        val chain = generateSequence(exception as Throwable) { it.cause }
            .joinToString(" → ") { "${it.javaClass.simpleName}(${it.message})" }
        Log.e(
            "NfcReading",
            "NFC reading failed – searchRestartCount=$searchRestartCount chain: $chain",
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

        val restarts = searchRestartCount
        hasContactedChip = false
        searchRestartCount = 0
        nfcReadingViewModel.reportNfcAttemptFailure(exception, restarts)
    }
}
