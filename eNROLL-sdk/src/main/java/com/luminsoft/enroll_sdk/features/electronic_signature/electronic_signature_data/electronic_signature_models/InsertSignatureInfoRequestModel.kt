package com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_data.electronic_signature_models

import com.google.gson.annotations.SerializedName

open class InsertSignatureInfoRequestModel {
    @SerializedName("status")
    internal var status: Int? = null

    @SerializedName("nationalId")
    internal var nationalId: String? = null

    @SerializedName("phoneNumber")
    internal var phoneNumber: String? = null

    @SerializedName("email")
    internal var email: String? = null
}


