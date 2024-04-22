package com.luminsoft.enroll_sdk.features.email.email_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.email.email_domain.repository.EmailRepository

class ApproveMailsUseCase(private val mailsRepository: EmailRepository) :
    UseCase<Either<SdkFailure, Null>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, Null> {
        return mailsRepository.approveMails()
    }
}


