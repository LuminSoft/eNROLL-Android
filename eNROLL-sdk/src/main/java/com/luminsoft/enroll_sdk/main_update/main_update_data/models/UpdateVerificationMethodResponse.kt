package com.luminsoft.enroll_sdk.main_update.main_update_data.models

import com.google.gson.annotations.SerializedName

open class UpdateVerificationMethodResponse {
    @SerializedName("value")
    internal var authStepId: Int? = null
}
