package com.luminsoft.ekyc_android_sdk.innovitices.faceautocapture

data class FaceAutoCaptureState(
    val isProcessing: Boolean = false,
    val result: FaceAutoCaptureResult? = null,
    val errorMessage: String? = null,
)
