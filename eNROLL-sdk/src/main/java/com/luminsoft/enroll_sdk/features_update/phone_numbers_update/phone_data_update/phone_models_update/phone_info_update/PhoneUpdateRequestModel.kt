package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_info_update

import com.google.gson.annotations.SerializedName

open class PhoneUpdateRequestModel {

    @SerializedName("phoneNumber")
    internal var phoneNumber: String? = null

    @SerializedName("code")
    internal var code: String? = null
}
