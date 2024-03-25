package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.phone_info

import com.google.gson.annotations.SerializedName

open class PhoneInfoRequestModel {

    @SerializedName("phoneNumber")
    internal var phoneNumber: String? = null

    @SerializedName("code")
    internal var code: String? = null

}
