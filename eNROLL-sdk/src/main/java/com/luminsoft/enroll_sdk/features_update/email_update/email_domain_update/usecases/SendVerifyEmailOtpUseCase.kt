package com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_info_update.MailUpdateRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.repository.EmailRepositoryUpdate

class SendVerifyEmailOtpUseCase(private val mailsRepository: EmailRepositoryUpdate) :
    UseCase<Either<SdkFailure, Null>, UpdateMailAddUseCaseParams> {

    override suspend fun call(params: UpdateMailAddUseCaseParams): Either<SdkFailure, Null> {
        val mailUpdateRequestModel = MailUpdateRequestModel()
        mailUpdateRequestModel.email = params.email
        return mailsRepository.sendVerifyEmailOtp(mailUpdateRequestModel)
    }
}



