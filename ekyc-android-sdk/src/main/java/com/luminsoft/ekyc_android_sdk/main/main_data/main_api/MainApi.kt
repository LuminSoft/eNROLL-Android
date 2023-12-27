    package com.luminsoft.ekyc_android_sdk.main.main_data.main_api


import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenResponse
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.get_onboaring_configurations.StepModel
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.initialize_request.InitializeRequestResponse

import retrofit2.Response
import retrofit2.http.*

interface MainApi {
    @POST("/api/v1/Auth/GenerateOnboardingSessionToken")
    suspend fun generateOnboardingSessionToken(@Body request : GenerateOnboardingSessionTokenRequest): Response<GenerateOnboardingSessionTokenResponse>
    @GET("/api/v1/Configuration/GetStepsConfiguration")
    suspend fun getOnBoardingStepsConfigurations(): Response<List<StepModel>>
    @POST("/api/v1/onboarding/Request/InitializeRequest")
    suspend fun initializeRequest(@Body request: InitializeRequestRequest): Response<InitializeRequestResponse>
}