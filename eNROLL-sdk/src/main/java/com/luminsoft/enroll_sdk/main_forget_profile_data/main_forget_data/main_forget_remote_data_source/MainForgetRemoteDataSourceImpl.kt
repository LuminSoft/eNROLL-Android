package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_api.MainForgetApi
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.generate_forget_token.GenerateForgetTokenRequest
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.generate_forget_token.VerifyPasswordRequestModel

class MainForgetRemoteDataSourceImpl(
    private val network: com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource,
    private val mainApi: MainForgetApi
) :
    MainForgetRemoteDataSource {

    override suspend fun generateForgetRequestSessionToken(request: GenerateOnboardingSessionTokenRequest): BaseResponse<Any> {

        return network.apiRequest { mainApi.generateForgetRequestSessionToken(request) }

    }

    override suspend fun getForgetStepsConfigurations(): BaseResponse<Any> {

        return network.apiRequest { mainApi.getForgetStepsConfigurations() }

    }

    override suspend fun generateForgetRequestTokenForStep(request: GenerateForgetTokenRequest): BaseResponse<Any> {

        return network.apiRequest { mainApi.generateForgetRequestTokenForStep(request) }

    }

    override suspend fun initializeForgetRequest(stepId: Int): BaseResponse<Any> {

        return network.apiRequest { mainApi.initializeForgetRequest(stepId) }
    }

    override suspend fun verifyPassword(request: VerifyPasswordRequestModel): BaseResponse<Any> {

        return network.apiRequest { mainApi.verifyPassword(request) }
    }


}






