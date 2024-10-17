package com.luminsoft.enroll_sdk.features_forget.forget_password.forget_password_data.forget_password_models

import com.google.gson.annotations.SerializedName

data class GetDefaultEmailResponseModel (
    @SerializedName("value") var defaultEmail: String? = null

)