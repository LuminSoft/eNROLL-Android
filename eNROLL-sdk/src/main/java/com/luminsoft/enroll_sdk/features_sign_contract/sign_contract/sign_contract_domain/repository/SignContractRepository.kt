package com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_domain.repository

import ValidateOTPRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_models.SignContractSendOTPResponseModel

interface SignContractRepository {
    suspend fun sendSignContractOtp(): Either<SdkFailure, SignContractSendOTPResponseModel>
    suspend fun validateOTPSignContract(request: ValidateOTPRequestModel): Either<SdkFailure, Null>
}