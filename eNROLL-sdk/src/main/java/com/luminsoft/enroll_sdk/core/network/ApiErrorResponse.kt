package com.luminsoft.enroll_sdk.core.network

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse (
    @SerializedName("errorCode") val statusCode: Int? =0,
    @SerializedName("message") val message: String? = "",
)

