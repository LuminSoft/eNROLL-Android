package com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.SecurityQuestionsRequestModel
import retrofit2.http.Body

interface SecurityQuestionsRemoteDataSource {
    suspend fun getSecurityQuestions(): BaseResponse<Any>
    suspend fun postSecurityQuestions(@Body request: /*List<SecurityQuestionsRequestModel>*/String): BaseResponse<Any>
}