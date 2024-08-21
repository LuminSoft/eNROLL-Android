package com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_update_old_mail

import com.google.gson.annotations.SerializedName

open class MailUpdateOldMailRequestModel {

    @SerializedName("id")
    internal var id: Int? = null

    @SerializedName("updatedEmail")
    internal var updatedEmail: String? = null
}
