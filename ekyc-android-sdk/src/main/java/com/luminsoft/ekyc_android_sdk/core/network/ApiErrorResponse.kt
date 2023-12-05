package com.luminsoft.ekyc_android_sdk.core.network

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse (
    @SerializedName("errorCode") val statusCode: Int? =0,
    @SerializedName("message") val message: String? = "",
)

