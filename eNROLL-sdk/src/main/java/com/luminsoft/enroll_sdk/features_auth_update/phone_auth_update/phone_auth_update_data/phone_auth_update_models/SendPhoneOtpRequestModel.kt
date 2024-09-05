package com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models

import com.google.gson.annotations.SerializedName

open class SendPhoneOtpRequestModel {

    @SerializedName("updateStep")
    internal var updateStep: Int? = null

}