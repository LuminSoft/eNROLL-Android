package com.luminsoft.ekyc_android.models

data class GenerateTokenResponse(
    val token: String,
    val expirationDate: String
)