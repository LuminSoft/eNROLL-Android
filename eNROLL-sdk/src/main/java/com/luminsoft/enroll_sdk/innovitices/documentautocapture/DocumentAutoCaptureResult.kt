package com.luminsoft.enroll_sdk.innovitices.documentautocapture
import android.graphics.Bitmap

data class DocumentAutoCaptureResult(
    val bitmap: Bitmap,
    val documentAutoCaptureResult: com.innovatrics.dot.document.autocapture.DocumentAutoCaptureResult,
)
