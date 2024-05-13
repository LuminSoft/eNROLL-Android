package com.luminsoft.enroll_sdk.features.security_questions.security_questions_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.GetSecurityQuestionsResponseModel
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.SecurityQuestionsRequestModel

interface SecurityQuestionsRepository {
    suspend fun getSecurityQuestions(): Either<SdkFailure, List<GetSecurityQuestionsResponseModel>>
    suspend fun postSecurityQuestions(request: /*List<SecurityQuestionsRequestModel>*/String): Either<SdkFailure, Null>

}