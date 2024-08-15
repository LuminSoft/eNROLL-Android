package com.luminsoft.enroll_sdk.features.email.email_data.email_models.verified_mails

import com.google.gson.annotations.SerializedName

open class GetVerifiedMailsResponseModel {

    @SerializedName("email")
    internal var email: String? = null

    @SerializedName("id")
    internal var id: Int? = null

    @SerializedName("isDefault")
    internal var isDefault: Boolean? = null

}
