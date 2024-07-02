package com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_api.SecurityQuestionsApi
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.SecurityQuestionsRequestModel


class SecurityQuestionsRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val securityQuestionsApi: SecurityQuestionsApi
) :
    SecurityQuestionsRemoteDataSource {

    override suspend fun getSecurityQuestions(): BaseResponse<Any> {
        return network.apiRequest { securityQuestionsApi.getSecurityQuestions() }
    }

    override suspend fun postSecurityQuestions(request: List<SecurityQuestionsRequestModel>): BaseResponse<Any> {
        return network.apiRequest { securityQuestionsApi.postSecurityQuestions(request) }
    }
}






