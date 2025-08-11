package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_sign_contract_steps

import com.google.gson.annotations.SerializedName


data class StepSignContractModel(

    @SerializedName("contractId") var contractId: Int? = null,
    @SerializedName("contractVersionNumber") var contractVersionNumber: Int? = null,
    @SerializedName("contractVersionDetailModel") var contractVersionDetailModel: ArrayList<ContractFileModel> = arrayListOf()

)
