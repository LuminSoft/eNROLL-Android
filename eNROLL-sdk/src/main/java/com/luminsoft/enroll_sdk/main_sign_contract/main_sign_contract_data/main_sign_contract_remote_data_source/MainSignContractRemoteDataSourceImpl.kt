package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_remote_data_source

import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_api.MainSignContractApi
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class MainSignContractRemoteDataSourceImpl(
    private val network: com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource,
    private val mainApi: MainSignContractApi
) :
    MainSignContractRemoteDataSource {

    override suspend fun generateSignContractSessionToken(request: GenerateOnboardingSessionTokenRequest): BaseResponse<Any> {

        return network.apiRequest {
            mainApi.generateSignContractRequestSessionToken(
                request.tenantId!!.toRequestBody("text/plain".toMediaTypeOrNull()),
                request.tenantSecret!!.toRequestBody("text/plain".toMediaTypeOrNull()),
                request.applicantId!!.toRequestBody("text/plain".toMediaTypeOrNull()),
                request.contractTemplateId!!.toRequestBody("text/plain".toMediaTypeOrNull()),
                request.signContractMode!!.toRequestBody("text/plain".toMediaTypeOrNull()),
                request.signContractApproach!!.toRequestBody("text/plain".toMediaTypeOrNull()),
                request.contractParams!!.toRequestBody("text/plain".toMediaTypeOrNull()),
            )
        }

    }

    override suspend fun getSignContractStepsConfigurations(): BaseResponse<Any> {

        return network.apiRequest { mainApi.getSignContractStepsConfigurations() }

    }

    override suspend fun initializeSignContractRequest(request: InitializeRequestRequest): BaseResponse<Any> {

        return network.apiRequest { mainApi.initializeSignContractRequest(request) }

    }
}






