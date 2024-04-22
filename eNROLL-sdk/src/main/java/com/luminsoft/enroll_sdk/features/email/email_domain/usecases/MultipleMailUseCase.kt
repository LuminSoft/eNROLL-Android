package com.luminsoft.enroll_sdk.features.email.email_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.verified_mails.GetVerifiedMailsResponseModel
import com.luminsoft.enroll_sdk.features.email.email_domain.repository.EmailRepository

class MultipleMailUseCase(private val mailsRepository: EmailRepository) :
    UseCase<Either<SdkFailure, List<GetVerifiedMailsResponseModel>>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, List<GetVerifiedMailsResponseModel>> {
        return mailsRepository.getVerifiedMails()
    }
}


