package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.verified_phones

import com.google.gson.annotations.SerializedName

open class GetVerifiedPhonesResponseModel {

    @SerializedName("phoneNumber")
    internal var phoneNumber: String? = null

    @SerializedName("isDefault")
    internal var isDefault: Boolean? = null

    @SerializedName("id")
    internal var id: Int? = null
}
