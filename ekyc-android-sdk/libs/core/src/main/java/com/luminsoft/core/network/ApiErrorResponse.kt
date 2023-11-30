package com.luminsoft.core.network

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse (
    @SerializedName("statusCode") val statusCode: Int? =0,
    @SerializedName("operationStatus") val operationStatus: Int? = 0,
    @SerializedName("operationMessage") val operationMessage: String? = "",
    @SerializedName("error") val errorList: List<String>? = emptyList() ,
)

