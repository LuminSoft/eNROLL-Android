package com.luminsoft.ekyc_android_sdk.core.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


open class ApiBaseResponse<T> (
    @SerializedName("data")
    @Expose
    val data:T
)