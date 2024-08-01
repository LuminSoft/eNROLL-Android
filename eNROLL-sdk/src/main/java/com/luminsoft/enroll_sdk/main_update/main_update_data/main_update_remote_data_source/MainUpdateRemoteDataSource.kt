package com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_remote_data_source
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest

interface MainUpdateRemoteDataSource {
    suspend fun generateUpdateRequestSessionToken(request: GenerateOnboardingSessionTokenRequest): BaseResponse<Any>
    suspend fun getUpdateStepsConfigurations(): BaseResponse<Any>

}