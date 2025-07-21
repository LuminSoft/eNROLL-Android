package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_models

import com.google.gson.annotations.SerializedName

open class GetCurrentContractRequestModel {

    @SerializedName("contractId")
    internal var contractId: Int? = null

    @SerializedName("contractVersionNumber")
    internal var contractVersionNumber: Int? = null

    @SerializedName("currentApproach")
    internal var currentApproach: Int? = null

    @SerializedName("currentText")
    internal var currentText: Int? = null

    @SerializedName("type")
    internal var type: Int? = null

}