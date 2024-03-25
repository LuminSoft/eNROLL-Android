package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.validate_otp

import com.google.gson.annotations.SerializedName

open class ValidateOTPRequestModel {

    @SerializedName("otp")
    internal var otp: String? = null

}
