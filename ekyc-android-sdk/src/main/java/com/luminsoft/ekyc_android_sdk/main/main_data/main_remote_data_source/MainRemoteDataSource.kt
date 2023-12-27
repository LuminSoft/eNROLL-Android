package com.luminsoft.ekyc_android_sdk.main.main_data.main_remote_data_source

import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest

interface  MainRemoteDataSource  {
    suspend fun generateOnboardingSessionToken(request: GenerateOnboardingSessionTokenRequest): BaseResponse<Any>
    suspend fun getOnBoardingStepsConfigurations(): BaseResponse<Any>
    suspend fun initializeRequest(request: InitializeRequestRequest): BaseResponse<Any>

}