package com.luminsoft.enroll_sdk.features.email.email_domain.usecases

import ValidateOTPRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.email.email_domain.repository.EmailRepository

class ValidateOtpMailUseCase(private val mailsRepository: EmailRepository) :
    UseCase<Either<SdkFailure, Null>, ValidateOtpMailUseCaseParams> {

    override suspend fun call(params: ValidateOtpMailUseCaseParams): Either<SdkFailure, Null> {
        val validateOTPRequestModel = ValidateOTPRequestModel()
        validateOTPRequestModel.otp = params.otp
        return mailsRepository.validateOTP(validateOTPRequestModel)
    }
}

data class ValidateOtpMailUseCaseParams(
    val otp: String? = null
)
