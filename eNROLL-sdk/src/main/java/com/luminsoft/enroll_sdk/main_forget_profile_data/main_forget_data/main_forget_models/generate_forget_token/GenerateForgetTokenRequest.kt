package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.generate_forget_token

import com.google.gson.annotations.SerializedName

open class GenerateForgetTokenRequest {

    @SerializedName("step")
    internal var step: Int? = null

    @SerializedName("nationalIdOrPassportNumber")
    internal var nationalIdOrPassportNumber: String? = null


}
