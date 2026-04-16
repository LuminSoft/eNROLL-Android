package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.util.Log
import com.innovatrics.dot.nfc.reader.NfcTravelDocumentReaderResult
import com.luminsoft.enroll_sdk.innovitices.image.createBitmap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateUiResultUseCase(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    suspend operator fun invoke(nfcTravelDocumentReaderResult: NfcTravelDocumentReaderResult): NfcReadingResult = withContext(context = defaultDispatcher) {
        val travelDoc = nfcTravelDocumentReaderResult.travelDocument
        val faceImage = travelDoc.encodedIdentificationFeaturesFace.faceImage
        Log.d("NfcReading", "NFC result – faceImage present=${faceImage != null}" +
            " format=${faceImage?.format} bytesSize=${faceImage?.bytes?.size}")

        val faceBitmap = faceImage?.createBitmap()
        Log.d("NfcReading", "NFC result – faceBitmap decoded=${faceBitmap != null}" +
            if (faceBitmap != null) " ${faceBitmap.width}x${faceBitmap.height}" else "")

        NfcReadingResult(
            nfcTravelDocumentReaderResult = nfcTravelDocumentReaderResult,
            faceBitmap = faceBitmap,
        )
    }
}
