package com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.email.email_domain.usecases.MakeDefaultMailUseCaseParams
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.repository.EmailRepositoryUpdate

class MakeDefaultMailUpdateUseCase(private val mailsRepository: EmailRepositoryUpdate) :
    UseCase<Either<SdkFailure, Null>, MakeDefaultMailUseCaseParams> {

    override suspend fun call(params: MakeDefaultMailUseCaseParams): Either<SdkFailure, Null> {
        val makeDefaultRequestModel = MakeDefaultRequestModel()
        makeDefaultRequestModel.email = params.email
        return mailsRepository.makeDefault(makeDefaultRequestModel)
    }
}

