package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_update_old_phone

import com.google.gson.annotations.SerializedName

open class PhoneUpdateOldPhoneRequestModel {

    @SerializedName("id")
    internal var id: Int? = null

    @SerializedName("phoneNumber")
    internal var phoneNumber: String? = null
}
