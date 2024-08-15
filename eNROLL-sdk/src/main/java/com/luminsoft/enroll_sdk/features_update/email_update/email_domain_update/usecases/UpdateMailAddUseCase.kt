package com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_add_new_update.MailUpdateAddNewResponseModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_info_update.MailUpdateRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.repository.EmailRepositoryUpdate

class UpdateMailAddUseCase(private val mailsRepository: EmailRepositoryUpdate) :
    UseCase<Either<SdkFailure, MailUpdateAddNewResponseModel>, UpdateMailAddUseCaseParams> {

    override suspend fun call(params: UpdateMailAddUseCaseParams): Either<SdkFailure, MailUpdateAddNewResponseModel> {
        val mailUpdateRequestModel = MailUpdateRequestModel()
        mailUpdateRequestModel.email = params.email
        return mailsRepository.updateMailAdd(mailUpdateRequestModel)
    }
}

data class UpdateMailAddUseCaseParams(
    val email: String? = null
)

