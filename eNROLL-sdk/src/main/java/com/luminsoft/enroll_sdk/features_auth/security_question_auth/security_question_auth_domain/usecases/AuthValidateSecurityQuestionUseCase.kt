package com.luminsoft.enroll_sdk.features_auth.security_question_auth.security_question_auth_domain.usecases

import SecurityQuestionAuthRepository
import SecurityQuestionAuthRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class ValidateSecurityQuestionUseCase(private val securityQuestionAuthRepository: SecurityQuestionAuthRepository) :
    UseCase<Either<SdkFailure, Null>, SecurityQuestionAuthRequestModel> {

    override suspend fun call(params: SecurityQuestionAuthRequestModel): Either<SdkFailure, Null> {
        return securityQuestionAuthRepository.validateSecurityQuestion(params)
    }
}
