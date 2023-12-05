package com.luminsoft.ekyc_android_sdk.main.main_data.main_remote_data_source
import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.ekyc_android_sdk.main.main_data.main_api.MainApi
import com.luminsoft.ekyc_android_sdk.main.main_data.main_remote_data_source.MainRemoteDataSource


class  MainRemoteDataSourceImpl (private val network: com.luminsoft.ekyc_android_sdk.core.network.BaseRemoteDataSource, private val mainApi: MainApi):
    MainRemoteDataSource {

    override suspend fun generateOnboardingSessionToken(request: GenerateOnboardingSessionTokenRequest, ): BaseResponse<Any> {

            return network.apiRequest { mainApi.generateOnboardingSessionToken(request) }

    }
}






