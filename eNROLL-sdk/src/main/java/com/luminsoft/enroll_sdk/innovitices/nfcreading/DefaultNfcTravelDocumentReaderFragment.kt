package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.util.Log
import androidx.fragment.app.activityViewModels
import com.innovatrics.dot.nfc.reader.NfcTravelDocumentReaderResult
import com.innovatrics.dot.nfc.reader.ui.NfcTravelDocumentReaderFragment

class DefaultNfcTravelDocumentReaderFragment : NfcTravelDocumentReaderFragment() {

    private val nfcReadingViewModel: NfcReadingViewModel by activityViewModels { NfcReadingViewModelFactory(requireActivity().application) }

    override fun provideConfiguration(): Configuration {
        return nfcReadingViewModel.state.value.configuration!!
    }

    override fun onSucceeded(result: NfcTravelDocumentReaderResult) {
        Log.d("NfcReading", "NFC reading succeeded")
        nfcReadingViewModel.process(result)
    }

    override fun onFailed(exception: Exception) {
        Log.e("NfcReading", "NFC reading failed", exception)
        nfcReadingViewModel.reportNfcAttemptFailure(exception)
    }
}
