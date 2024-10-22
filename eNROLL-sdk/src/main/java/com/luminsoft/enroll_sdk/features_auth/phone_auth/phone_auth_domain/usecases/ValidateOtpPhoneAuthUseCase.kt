package com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_domain.usecases

import ValidateOTPRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_domain.repository.PhoneAuthRepository

class ValidateOtpPhoneAuthUseCase(private val phoneRepository: PhoneAuthRepository) :
    UseCase<Either<SdkFailure, Null>, ValidateOtpPhoneAuthUseCaseParams> {

    override suspend fun call(params: ValidateOtpPhoneAuthUseCaseParams): Either<SdkFailure, Null> {
        val validateOTPRequestModel = ValidateOTPRequestModel()
        validateOTPRequestModel.otp = params.otp
        return phoneRepository.validateOTPPhoneAuth(validateOTPRequestModel)
    }
}

data class ValidateOtpPhoneAuthUseCaseParams(
    val otp: String? = null
)
