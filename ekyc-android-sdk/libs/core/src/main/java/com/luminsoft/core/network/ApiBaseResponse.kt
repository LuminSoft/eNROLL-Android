package com.luminsoft.core.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


open class ApiBaseResponse<T> (
    @SerializedName("statusCode") val statusCode: Int? = 0,
    @SerializedName("operationStatus") val operationStatus: Int? = 0,
    @SerializedName("operationMessage") val operationMessage: String?="",
    @SerializedName("data")
    @Expose
    val data:T
)