package com.luminsoft.ekyc_android_sdk.innovitices.documentautocapture

import android.graphics.Bitmap
import com.innovatrics.dot.document.detection.DocumentDetector

data class DocumentAutoCaptureResult(
    val bitmap: Bitmap,
    val documentDetectorResult: DocumentDetector.Result,
)
