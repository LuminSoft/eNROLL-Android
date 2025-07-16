package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_api.MainAuthApi

class MainSignContractRemoteDataSourceImpl(
    private val network: com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource,
    private val mainApi: MainAuthApi
) :
    MainSignContractRemoteDataSource {

    override suspend fun generateSignContractSessionToken(request: GenerateOnboardingSessionTokenRequest): BaseResponse<Any> {

        return network.apiRequest { mainApi.generateAuthRequestSessionToken(request) }

    }

    override suspend fun getSignContractStepsConfigurations(): BaseResponse<Any> {

        return network.apiRequest { mainApi.getAuthStepsConfigurations() }

    }

    override suspend fun initializeSignContractRequest(request: InitializeRequestRequest): BaseResponse<Any> {

        return network.apiRequest { mainApi.initializeAuthRequest(request) }

    }
}






