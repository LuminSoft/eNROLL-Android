package com.luminsoft.ekyc_android.models

data class GenerateTokenRequest(
    val tenantId: String,
    val tenantSecret: String,
    val deviceId: String,
    val correlationId: String
)