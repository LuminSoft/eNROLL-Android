package com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_repository


import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.GetSecurityQuestionsResponseModel
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.SecurityQuestionsRequestModel
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_domain.repository.SecurityQuestionsRepository
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_remote_data_source.SecurityQuestionsRemoteDataSource

class SecurityQuestionsRepositoryImplementation(private val securityQuestionsRemoteDataSource: SecurityQuestionsRemoteDataSource) :
    SecurityQuestionsRepository {

    override suspend fun getSecurityQuestions(): Either<SdkFailure, List<GetSecurityQuestionsResponseModel>> {
        return when (val response = securityQuestionsRemoteDataSource.getSecurityQuestions()) {
            is BaseResponse.Success -> {
                Either.Right((response.data as List<GetSecurityQuestionsResponseModel>))
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun postSecurityQuestions(request: List<SecurityQuestionsRequestModel>): Either<SdkFailure, Null> {
        return when (val response =
            securityQuestionsRemoteDataSource.postSecurityQuestions(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

