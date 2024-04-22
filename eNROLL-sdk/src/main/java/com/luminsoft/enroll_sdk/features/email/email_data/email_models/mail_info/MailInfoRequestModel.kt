package com.luminsoft.enroll_sdk.features.email.email_data.email_models.mail_info

import com.google.gson.annotations.SerializedName

open class MailInfoRequestModel {

    @SerializedName("email")
    internal var email: String? = null

}
