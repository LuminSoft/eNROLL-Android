package com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest

interface MainAuthRemoteDataSource {
    suspend fun generateAuthSessionToken(request: GenerateOnboardingSessionTokenRequest): BaseResponse<Any>
    suspend fun getAuthStepsConfigurations(): BaseResponse<Any>
    suspend fun initializeAuthRequest(request: InitializeRequestRequest): BaseResponse<Any>

}