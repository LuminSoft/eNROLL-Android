package com.luminsoft.enroll_sdk.features_auth.security_question_auth.security_questions__auth_data.security_question_auth_api

import GetSecurityQuestionAuthResponseModel
import SecurityQuestionAuthRequestModel
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SecurityQuestionAuthApi {
    @GET("api/v1/authentication/CheckSecurityQuestions/GetRandomQuestion")
    suspend fun getSecurityQuestion(): Response<GetSecurityQuestionAuthResponseModel>

    @POST("api/v1/authentication/CheckSecurityQuestions/Validate")
    suspend fun validateSecurityQuestion(@Body request: SecurityQuestionAuthRequestModel): Response<BasicResponseModel>
}