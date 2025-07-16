package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases

import ValidateOTPRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.repository.LowRiskFRARepository

class ValidateOtpLowRiskFRAUseCase(private val phoneRepository: LowRiskFRARepository) :
    UseCase<Either<SdkFailure, Null>, ValidateOtpLowRiskFRAUseCaseParams> {

    override suspend fun call(params: ValidateOtpLowRiskFRAUseCaseParams): Either<SdkFailure, Null> {
        val validateOTPRequestModel = ValidateOTPRequestModel()
        validateOTPRequestModel.otp = params.otp
        return phoneRepository.validateOTPLowRiskFRA(validateOTPRequestModel)
    }
}

data class ValidateOtpLowRiskFRAUseCaseParams(
    val otp: String? = null
)
