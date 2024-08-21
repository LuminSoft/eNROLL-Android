package com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_info_update

import com.google.gson.annotations.SerializedName

open class MailUpdateRequestModel {

    @SerializedName("email")
    internal var email: String? = null
}
