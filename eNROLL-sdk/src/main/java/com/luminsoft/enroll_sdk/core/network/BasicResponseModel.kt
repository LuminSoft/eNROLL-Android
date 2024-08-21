package com.luminsoft.enroll_sdk.core.network//
import com.google.gson.annotations.SerializedName

data class BasicResponseModel(
    @SerializedName("status") var status: Int? = null
)
