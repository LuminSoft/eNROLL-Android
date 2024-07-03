package com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_api


import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.StepModel
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestResponse
import com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_models.get_auth_configurations.StepAuthModel

import retrofit2.Response
import retrofit2.http.*

interface MainAuthApi {
    @POST("/api/v1/Auth/GenerateAuthRequestSessionToken")
    suspend fun generateAuthRequestSessionToken(@Body request: GenerateOnboardingSessionTokenRequest): Response<GenerateOnboardingSessionTokenResponse>

    @GET("/api/v1/LevelOfTrust/GetCurrentSessionLevelOfTrustSteps")
    suspend fun getAuthStepsConfigurations(): Response<List<StepAuthModel>>

    @POST("/api/v1/authentication/AuthenticationRequest/InitializeRequest")
    suspend fun initializeAuthRequest(@Body request: InitializeRequestRequest): Response<InitializeRequestResponse>
}