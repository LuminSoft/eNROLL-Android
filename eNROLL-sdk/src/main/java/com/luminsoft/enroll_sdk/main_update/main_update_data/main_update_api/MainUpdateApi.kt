package com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_api

import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenResponse
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_models.get_update_configurations.StepUpdateModel
import com.luminsoft.enroll_sdk.main_update.main_update_data.models.UpdateVerificationMethodResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MainUpdateApi {
    @POST("api/v1/Auth/GenerateUpdateRequestSessionToken")
    suspend fun generateUpdateRequestSessionToken(@Body request: GenerateOnboardingSessionTokenRequest): Response<GenerateOnboardingSessionTokenResponse>

    @GET("api/v1/update/UpdateRequest/GetCurrentRequestSteps")
    suspend fun getUpdateStepsConfigurations(): Response<List<StepUpdateModel>>

    @POST("api/v1/update/UpdateRequest/Initialize/{updateStepId}")
    suspend fun updateStepsInitRequest(@Path("updateStepId") updateStepId: Int): Response<BasicResponseModel>

    @GET("api/v1/update/UpdateRequest/GetUpdateVerificationMethod")
    suspend fun getUpdateAuthenticationMethod(@Query("updateStep") stepId: Int): Response<UpdateVerificationMethodResponse>

}