package com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_domain.usecases

import ValidateOTPRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_domain.repository.SignContractRepository

class ValidateOtpSignContractUseCase(private val signContractRepository: SignContractRepository) :
    UseCase<Either<SdkFailure, Null>, ValidateOtpSignContractUseCaseParams> {

    override suspend fun call(params: ValidateOtpSignContractUseCaseParams): Either<SdkFailure, Null> {
        val validateOTPRequestModel = ValidateOTPRequestModel()
        validateOTPRequestModel.otp = params.otp
        return signContractRepository.validateOTPSignContract(validateOTPRequestModel)
    }
}

data class ValidateOtpSignContractUseCaseParams(
    val otp: String? = null
)
