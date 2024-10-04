package com.luminsoft.enroll_sdk.innovitices.faceautocapture

import android.graphics.Bitmap

data class FaceAutoCaptureResult(
    val bitmap: Bitmap,
    val faceAutoCaptureResult: com.innovatrics.dot.face.autocapture.FaceAutoCaptureResult,
)
