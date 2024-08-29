package com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_domain.usecases

import ValidateOTPRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_domain.repository.MailAuthRepository

class ValidateOtpMailAuthUseCase(private val mailsRepository: MailAuthRepository) :
    UseCase<Either<SdkFailure, Null>, ValidateOtpMailAuthUseCaseParams> {

    override suspend fun call(params: ValidateOtpMailAuthUseCaseParams): Either<SdkFailure, Null> {
        val validateOTPRequestModel = ValidateOTPRequestModel()
        validateOTPRequestModel.otp = params.otp
        return mailsRepository.validateOTPMailAuth(validateOTPRequestModel)
    }
}

data class ValidateOtpMailAuthUseCaseParams(
    val otp: String? = null
)
