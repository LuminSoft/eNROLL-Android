package com.luminsoft.enroll_sdk.features.email.email_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.email.email_domain.repository.EmailRepository

class DeleteMailUseCase(private val mailsRepository: EmailRepository) :
    UseCase<Either<SdkFailure, Null>, MakeDefaultMailUseCaseParams> {

    override suspend fun call(params: MakeDefaultMailUseCaseParams): Either<SdkFailure, Null> {
        val makeDefaultRequestModel = MakeDefaultRequestModel()
        makeDefaultRequestModel.email = params.email
        return mailsRepository.deleteMail(makeDefaultRequestModel)
    }
}


