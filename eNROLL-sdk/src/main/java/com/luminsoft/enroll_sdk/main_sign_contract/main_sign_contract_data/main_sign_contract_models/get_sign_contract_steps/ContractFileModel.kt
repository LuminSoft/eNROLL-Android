package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_sign_contract_steps

import com.google.gson.annotations.SerializedName

data class ContractFileModel(
    @SerializedName("sectionOrder") var sectionOrder: Int? = null,
    @SerializedName("signContractTextEnum") var signContractTextEnum: String? = null
)

