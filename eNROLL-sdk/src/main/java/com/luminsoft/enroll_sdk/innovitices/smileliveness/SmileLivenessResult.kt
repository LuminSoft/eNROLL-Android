package com.luminsoft.enroll_sdk.innovitices.smileliveness

import android.graphics.Bitmap

data class SmileLivenessResult(
    val bitmap: Bitmap,
    val videoContentBase64: String? = null,
)
