package com.luminsoft.enroll_sdk.features_auth_update.security_question_auth_update.security_questions_auth_update_data.security_question_auth_update_api

import GetSecurityQuestionAuthUpdateResponseModel
import SecurityQuestionAuthUpdateRequestModel
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SecurityQuestionAuthUpdateApi {
    @GET("api/v1/update/SecurityQuestionsUpdateAuthentication/GetSecurityQuestion")
    suspend fun getSecurityQuestion(@Query("updateStep") stepId: Int): Response<GetSecurityQuestionAuthUpdateResponseModel>

    @POST("api/v1/update/SecurityQuestionsUpdateAuthentication/VerifySecurityQuestion")
    suspend fun validateSecurityQuestion(@Body request: SecurityQuestionAuthUpdateRequestModel): Response<BasicResponseModel>
}