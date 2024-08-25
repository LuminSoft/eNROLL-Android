package com.luminsoft.enroll_sdk.features_auth_update.security_question_auth_update.security_question_auth_update_domain.usecases

import SecurityQuestionAuthUpdateRepository
import SecurityQuestionAuthUpdateRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class ValidateSecurityQuestionAuthUpdateUseCase(private val securityQuestionAuthRepository: SecurityQuestionAuthUpdateRepository) :
    UseCase<Either<SdkFailure, Null>, SecurityQuestionAuthUpdateRequestModel> {

    override suspend fun call(params: SecurityQuestionAuthUpdateRequestModel): Either<SdkFailure, Null> {
        return securityQuestionAuthRepository.validateSecurityQuestion(params)
    }
}
