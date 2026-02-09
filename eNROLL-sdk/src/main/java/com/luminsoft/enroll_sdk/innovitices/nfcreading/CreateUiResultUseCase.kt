package com.luminsoft.enroll_sdk.innovitices.nfcreading

import com.innovatrics.dot.nfc.reader.NfcTravelDocumentReaderResult
import com.luminsoft.enroll_sdk.innovitices.image.createBitmap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateUiResultUseCase(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    suspend operator fun invoke(nfcTravelDocumentReaderResult: NfcTravelDocumentReaderResult): NfcReadingResult = withContext(context = defaultDispatcher) {
        NfcReadingResult(
            nfcTravelDocumentReaderResult = nfcTravelDocumentReaderResult,
            faceBitmap = nfcTravelDocumentReaderResult.travelDocument.encodedIdentificationFeaturesFace.faceImage?.createBitmap(),
        )
    }
}
