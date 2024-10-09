package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_update_validate

import com.google.gson.annotations.SerializedName

open class PhoneUpdateValidatePhoneRequestModel {

    @SerializedName("id")
    internal var id: Int? = null

    @SerializedName("otp")
    internal var otp: String? = null

    @SerializedName("oldPhoneNumber")
    internal var oldPhoneNumber: String? = null
}
