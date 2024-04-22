package com.luminsoft.enroll_sdk.features.email.email_data.email_models.validate_otp
import com.google.gson.annotations.SerializedName

open class ValidateOTPRequestModel {

    @SerializedName("otp")
    internal var otp: String? = null

}
