package com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_remote_data_source

import ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface SignContractRemoteDataSource {
    suspend fun sendSignContractOtp(): BaseResponse<Any>
    suspend fun validateOTPSignContract(request: ValidateOTPRequestModel): BaseResponse<Any>

}