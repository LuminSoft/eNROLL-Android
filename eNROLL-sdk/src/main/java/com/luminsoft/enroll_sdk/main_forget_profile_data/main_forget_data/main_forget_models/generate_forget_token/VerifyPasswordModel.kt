package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.generate_forget_token

import com.google.gson.annotations.SerializedName

open class VerifyPasswordRequestModel {

    @SerializedName("updateStepId")
    internal var updateStepId: Int? = null

    @SerializedName("password")
    internal var password: String? = null

}
