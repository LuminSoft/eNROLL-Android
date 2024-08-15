package com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_update_validate

import com.google.gson.annotations.SerializedName

open class MailUpdateValidateMailRequestModel {

    @SerializedName("id")
    internal var id: Int? = null

    @SerializedName("otp")
    internal var otp: String? = null

    @SerializedName("oldEmail")
    internal var oldEmail: String? = null
}
