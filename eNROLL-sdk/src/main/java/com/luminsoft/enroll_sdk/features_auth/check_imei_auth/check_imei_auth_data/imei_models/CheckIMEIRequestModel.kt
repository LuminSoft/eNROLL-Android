package com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_data.imei_models

import com.google.gson.annotations.SerializedName

open class CheckIMEIRequestModel {

    @SerializedName("imei")
    internal var imei: String? = null

    @SerializedName("isFromWeb")
    internal var isFromWeb: Boolean? = null

}
