package com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_api

import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenResponse
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_models.get_update_configurations.StepUpdateModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MainUpdateApi {
    @POST("/api/v1/Auth/GenerateUpdateRequestSessionToken")
    suspend fun generateUpdateRequestSessionToken(@Body request: GenerateOnboardingSessionTokenRequest): Response<GenerateOnboardingSessionTokenResponse>

    @GET("/api/v1/update/UpdateRequest/GetCurrentRequestSteps")
    suspend fun getUpdateStepsConfigurations(): Response<List<StepUpdateModel>>
}