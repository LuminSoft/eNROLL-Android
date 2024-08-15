package com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_update_validate.MailUpdateValidateMailRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.repository.EmailRepositoryUpdate

class ValidateOtpMailUpdateUseCase(private val mailsRepository: EmailRepositoryUpdate) :
    UseCase<Either<SdkFailure, Null>, ValidateOtpMailUpdateUseCaseParams> {

    override suspend fun call(params: ValidateOtpMailUpdateUseCaseParams): Either<SdkFailure, Null> {
        val mailUpdateRequestModel = MailUpdateValidateMailRequestModel()
        mailUpdateRequestModel.otp = params.otp
        mailUpdateRequestModel.oldEmail = params.oldEmail
        mailUpdateRequestModel.id = params.id
        return mailsRepository.validateOTPUpdate(mailUpdateRequestModel)
    }
}

data class ValidateOtpMailUpdateUseCaseParams(
    val id: Int? = null,
    val otp: String? = null,
    val oldEmail: String? = null
)
