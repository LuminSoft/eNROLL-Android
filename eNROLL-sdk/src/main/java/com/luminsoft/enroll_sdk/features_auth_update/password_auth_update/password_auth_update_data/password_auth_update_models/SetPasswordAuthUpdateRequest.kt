package com.luminsoft.enroll_sdk.features_auth_update.password_auth_update.password_auth_update_data.password_auth_update_models

import com.google.gson.annotations.SerializedName

open class SetPasswordAuthUpdateRequest {

    @SerializedName("password")
    internal var password: String? = null

    @SerializedName("updateStepId")
    internal var updateStepId: Int? = null

}
