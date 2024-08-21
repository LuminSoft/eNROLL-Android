package com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_api

import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.GetSecurityQuestionsResponseModel
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.SecurityQuestionsRequestModel

import retrofit2.Response
import retrofit2.http.*

interface SecurityQuestionsApi {
    @GET("api/v1/onboarding/SecurityQuestionsInfo")
    suspend fun getSecurityQuestions(): Response<List<GetSecurityQuestionsResponseModel>>

    @POST("api/v1/onboarding/SecurityQuestionsInfo")
    suspend fun postSecurityQuestions(@Body request: List<@JvmSuppressWildcards SecurityQuestionsRequestModel>): Response<BasicResponseModel>
}