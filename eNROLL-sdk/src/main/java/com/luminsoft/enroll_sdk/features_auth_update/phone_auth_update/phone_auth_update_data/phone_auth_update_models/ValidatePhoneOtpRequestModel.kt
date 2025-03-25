package com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models

import com.google.gson.annotations.SerializedName

open class ValidatePhoneOtpRequestModel {

    @SerializedName("otp")
    internal var otp: String? = null

    @SerializedName("updateStep")
    internal var updateStep: Int? = null

}