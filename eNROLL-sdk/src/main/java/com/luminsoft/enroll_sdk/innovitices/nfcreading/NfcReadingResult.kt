package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.graphics.Bitmap
import com.innovatrics.dot.nfc.reader.NfcTravelDocumentReaderResult

data class NfcReadingResult(
    val nfcTravelDocumentReaderResult: NfcTravelDocumentReaderResult,
    val faceBitmap: Bitmap?,
)
