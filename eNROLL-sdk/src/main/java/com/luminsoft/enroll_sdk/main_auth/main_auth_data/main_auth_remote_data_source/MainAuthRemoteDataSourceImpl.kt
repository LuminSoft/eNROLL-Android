package com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_api.MainAuthApi

class MainAuthRemoteDataSourceImpl(
    private val network: com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource,
    private val mainApi: MainAuthApi
) :
    MainAuthRemoteDataSource {

    override suspend fun generateAuthSessionToken(request: GenerateOnboardingSessionTokenRequest): BaseResponse<Any> {

        return network.apiRequest { mainApi.generateAuthRequestSessionToken(request) }

    }

    override suspend fun getAuthStepsConfigurations(): BaseResponse<Any> {

        return network.apiRequest { mainApi.getAuthStepsConfigurations() }

    }

    override suspend fun initializeAuthRequest(request: InitializeRequestRequest): BaseResponse<Any> {

        return network.apiRequest { mainApi.initializeAuthRequest(request) }

    }
}






