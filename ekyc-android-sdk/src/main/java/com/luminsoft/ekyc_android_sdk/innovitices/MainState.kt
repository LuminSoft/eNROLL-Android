package com.luminsoft.ekyc_android_sdk.innovitices

import android.content.Intent

data class MainState(
    val isProcessing: Boolean = false,
    val intent: Intent? = null,
    val errorMessage: String? = null,
)
