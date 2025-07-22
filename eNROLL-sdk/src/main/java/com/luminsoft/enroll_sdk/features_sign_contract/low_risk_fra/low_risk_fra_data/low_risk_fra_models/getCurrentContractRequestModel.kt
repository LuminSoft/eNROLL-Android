package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_models

import com.google.gson.annotations.SerializedName

open class GetCurrentContractRequestModel {

    @SerializedName("contractId")
    internal var contractId: String? = null

    @SerializedName("contractVersionNumber")
    internal var contractVersionNumber: String? = null

    @SerializedName("currentApproach")
    internal var currentApproach: String? = null

    @SerializedName("currentText")
    internal var currentText: String? = null

    @SerializedName("type")
    internal var type: String? = null

}