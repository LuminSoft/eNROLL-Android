package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_api

import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenResponse
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.generate_forget_token.GenerateForgetTokenRequest
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.get_forget_configurations.StepForgetModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MainForgetApi {
    @POST("/api/v1/Auth/GenerateForgetSessionTokenForOrganization")
    suspend fun generateForgetRequestSessionToken(@Body request: GenerateOnboardingSessionTokenRequest): Response<GenerateOnboardingSessionTokenResponse>

    @GET("/api/v1/forget/ForgetRequest/GetCurrentRequestSteps")
    suspend fun getForgetStepsConfigurations(): Response<List<StepForgetModel>>

    @POST("/api/v1/Auth/GenerateForgetRequestTokenForStep")
    suspend fun generateForgetRequestTokenForStep(@Body request: GenerateForgetTokenRequest): Response<GenerateOnboardingSessionTokenResponse>

}