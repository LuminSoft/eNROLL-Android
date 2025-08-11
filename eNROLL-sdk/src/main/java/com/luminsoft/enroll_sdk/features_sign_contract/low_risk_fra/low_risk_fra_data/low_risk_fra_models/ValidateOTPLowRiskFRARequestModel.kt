package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_models

import com.google.gson.annotations.SerializedName

open class ValidateOTPLowRiskFRARequestModel {

    @SerializedName("otp")
    internal var otp: String? = null
}