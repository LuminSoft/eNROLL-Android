package com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_api.MainUpdateApi

class MainUpdateRemoteDataSourceImpl(
    private val network: com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource,
    private val mainApi: MainUpdateApi
) :
    MainUpdateRemoteDataSource {

    override suspend fun generateUpdateRequestSessionToken(request: GenerateOnboardingSessionTokenRequest): BaseResponse<Any> {

        return network.apiRequest { mainApi.generateUpdateRequestSessionToken(request) }

    }

    override suspend fun getUpdateStepsConfigurations(): BaseResponse<Any> {

        return network.apiRequest { mainApi.getUpdateStepsConfigurations() }

    }

    override suspend fun updateStepsInitRequest(updateStepId: Int): BaseResponse<Any> {

        return network.apiRequest { mainApi.updateStepsInitRequest(updateStepId) }

    }

}






