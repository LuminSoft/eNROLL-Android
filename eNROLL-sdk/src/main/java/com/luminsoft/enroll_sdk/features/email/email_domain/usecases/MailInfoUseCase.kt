package com.luminsoft.enroll_sdk.features.email.email_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.mail_info.MailInfoRequestModel
import com.luminsoft.enroll_sdk.features.email.email_domain.repository.EmailRepository

class MailInfoUseCase(private val mailsRepository: EmailRepository) :
    UseCase<Either<SdkFailure, Null>, MailInfoUseCaseParams> {

    override suspend fun call(params: MailInfoUseCaseParams): Either<SdkFailure, Null> {
        val mailInfoRequestModel = MailInfoRequestModel()
        mailInfoRequestModel.email = params.email
        return mailsRepository.mailInfo(mailInfoRequestModel)
    }
}
data class MailInfoUseCaseParams(
    val email: String? = null
)
