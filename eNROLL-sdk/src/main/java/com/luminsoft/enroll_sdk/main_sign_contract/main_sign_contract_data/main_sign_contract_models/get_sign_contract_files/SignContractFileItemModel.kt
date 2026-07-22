package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_sign_contract_files

import com.google.gson.annotations.SerializedName

data class SignContractFileItemModel(
    @SerializedName("signContractRequestId") var signContractRequestId: String? = null,
    @SerializedName("displayOrder") var displayOrder: Int? = null,
    @SerializedName("fileName") var fileName: String? = null,
    @SerializedName("contractTemplateId") var contractTemplateId: Int? = null
)
