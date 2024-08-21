package com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_add_new_update.MailUpdateAddNewResponseModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_update_old_mail.MailUpdateOldMailRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.repository.EmailRepositoryUpdate

class UpdateOldMailUpdateUseCase(private val mailsRepository: EmailRepositoryUpdate) :
    UseCase<Either<SdkFailure, MailUpdateAddNewResponseModel>, UpdateOldMailUseCaseParams> {

    override suspend fun call(params: UpdateOldMailUseCaseParams): Either<SdkFailure, MailUpdateAddNewResponseModel> {
        val mailUpdateRequestModel = MailUpdateOldMailRequestModel()
        mailUpdateRequestModel.id = params.id
        mailUpdateRequestModel.updatedEmail = params.updatedEmail
        return mailsRepository.updateOldMail(mailUpdateRequestModel)
    }
}

data class UpdateOldMailUseCaseParams(
    val id: Int? = null,
    val updatedEmail: String? = null
)

