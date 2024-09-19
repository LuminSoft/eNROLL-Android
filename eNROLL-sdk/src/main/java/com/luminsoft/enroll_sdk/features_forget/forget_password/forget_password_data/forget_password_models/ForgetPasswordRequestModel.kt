package com.luminsoft.enroll_sdk.features_forget.forget_password.forget_password_data.forget_password_models

import com.google.gson.annotations.SerializedName

open class ForgetPasswordRequestModel {

    @SerializedName("newPassword")
    internal var newPassword: String? = null

    @SerializedName("confirmedPassword")
    internal var confirmedPassword: String? = null

}