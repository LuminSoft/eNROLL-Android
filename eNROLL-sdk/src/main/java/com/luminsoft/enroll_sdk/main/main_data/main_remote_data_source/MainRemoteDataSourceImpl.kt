package com.luminsoft.enroll_sdk.main.main_data.main_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_api.MainApi
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest


class MainRemoteDataSourceImpl(
    private val network: com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource,
    private val mainApi: MainApi
) :
    MainRemoteDataSource {

    override suspend fun generateOnboardingSessionToken(request: GenerateOnboardingSessionTokenRequest): BaseResponse<Any> {

        return network.apiRequest { mainApi.generateOnboardingSessionToken(request) }

    }

    override suspend fun getOnBoardingStepsConfigurations(): BaseResponse<Any> {

        return network.apiRequest { mainApi.getOnBoardingStepsConfigurations() }

    }

    override suspend fun initializeRequest(request: InitializeRequestRequest): BaseResponse<Any> {

        return network.apiRequest { mainApi.initializeRequest(request) }

    }

    override suspend fun getApplicantId(): BaseResponse<Any> {

        return network.apiRequest { mainApi.getApplicantId() }

    }
}






