package com.luminsoft.enroll_sdk.features_auth.security_question_auth.security_question_auth_domain.usecases

import GetSecurityQuestionAuthResponseModel
import SecurityQuestionAuthRepository
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class GetSecurityQuestionAuthUseCase(private val securityQuestionAuthRepository: SecurityQuestionAuthRepository) :
    UseCase<Either<SdkFailure, GetSecurityQuestionAuthResponseModel>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, GetSecurityQuestionAuthResponseModel> {
        return securityQuestionAuthRepository.getSecurityQuestion()
    }
}