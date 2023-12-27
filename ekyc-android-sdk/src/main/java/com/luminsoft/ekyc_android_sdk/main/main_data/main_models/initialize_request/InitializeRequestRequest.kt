package com.luminsoft.ekyc_android_sdk.main.main_data.main_models.initialize_request

import com.google.gson.annotations.SerializedName

open class InitializeRequestRequest {

    @SerializedName("deviceModel")
    internal var deviceModel: String? = null

    @SerializedName("manufacturerName")
    internal var manufacturerName: String? = null

    @SerializedName("imei")
    internal var imei: String? = null

}
