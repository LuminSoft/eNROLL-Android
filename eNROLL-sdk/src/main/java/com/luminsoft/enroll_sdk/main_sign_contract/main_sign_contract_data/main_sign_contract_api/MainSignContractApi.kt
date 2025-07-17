package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_api

import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestResponse
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_auth_configurations.StepSignContractModel
import okhttp3.RequestBody

import retrofit2.Response
import retrofit2.http.*

interface MainSignContractApi {
    @Multipart
    @POST("api/v1/Auth/GenerateSignContractRequestSessionToken")
    suspend fun generateSignContractRequestSessionToken(
        @Part("TenantId") tenantId: RequestBody,
        @Part("TenantSecret") tenantSecret: RequestBody,
        @Part("ApplicantId") applicantId: RequestBody,
        @Part("ContractTemplateId") contractTemplateId: RequestBody,
        @Part("SignContractMode") signContractMode: RequestBody,
        @Part("SignContractApproach") signContractApproach: RequestBody,
        @Part("ContractParams") contractParams: RequestBody,
    ): Response<GenerateOnboardingSessionTokenResponse>

    @GET("api/v1/LevelOfTrust/GetCurrentSessionLevelOfTrustSteps")
    suspend fun getSignContractStepsConfigurations(): Response<List<StepSignContractModel>>

    @POST("api/v1/authentication/AuthenticationRequest/InitializeRequest")
    suspend fun initializeSignContractRequest(@Body request: InitializeRequestRequest): Response<InitializeRequestResponse>
}