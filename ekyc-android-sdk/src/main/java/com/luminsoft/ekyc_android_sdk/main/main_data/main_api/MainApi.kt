    package com.luminsoft.ekyc_android_sdk.main.main_data.main_api


import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenResponse

import retrofit2.Response
import retrofit2.http.*

interface MainApi {
    @POST("/api/v1/Auth/GenerateOnboardingSessionToken")
    suspend fun generateOnboardingSessionToken(@Body request : GenerateOnboardingSessionTokenRequest): Response<GenerateOnboardingSessionTokenResponse>
}