package com.luminsoft.enroll_sdk.features.security_questions.security_questions_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.GetSecurityQuestionsResponseModel
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_domain.repository.SecurityQuestionsRepository

class GetSecurityQuestionsUseCase  (private  val securityQuestionsRepository: SecurityQuestionsRepository):
    UseCase<Either<SdkFailure, List<GetSecurityQuestionsResponseModel>>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, List<GetSecurityQuestionsResponseModel>> {
        return securityQuestionsRepository.getSecurityQuestions()
    }
}