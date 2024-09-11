package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.generate_forget_token.GenerateForgetTokenRequest

interface MainForgetRemoteDataSource {
    suspend fun generateForgetRequestSessionToken(request: GenerateOnboardingSessionTokenRequest): BaseResponse<Any>
    suspend fun getForgetStepsConfigurations(): BaseResponse<Any>
    suspend fun generateForgetRequestTokenForStep(request: GenerateForgetTokenRequest): BaseResponse<Any>

}