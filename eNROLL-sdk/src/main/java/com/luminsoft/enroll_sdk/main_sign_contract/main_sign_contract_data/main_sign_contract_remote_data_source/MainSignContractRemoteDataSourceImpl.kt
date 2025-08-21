package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_remote_data_source

import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_api.MainSignContractApi
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.core.utils.EncryptionHelper
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class MainSignContractRemoteDataSourceImpl(
    private val network: com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource,
    private val mainApi: MainSignContractApi
) :
    MainSignContractRemoteDataSource {

    override suspend fun generateSignContractSessionToken(request: GenerateOnboardingSessionTokenRequest): BaseResponse<Any> {

        val jsonObject = JSONObject().apply {
            put("tenantId", request.tenantId)
            put("tenantSecret", request.tenantSecret)
            put("applicantId", request.applicantId)
            put("contractTemplateId", request.contractTemplateId)
            put("signContractMode", request.signContractMode)
            put("signContractApproach", request.signContractApproach)
            put("contractParams", request.contractParams)
        }
        val jsonString = jsonObject.toString()

        val encryptedObject = EncryptionHelper.encrypt(jsonString)
        return network.apiRequest {
            mainApi.generateSignContractRequestSessionToken(
                encryptedObject.toRequestBody("text/plain".toMediaTypeOrNull())
            )
        }

    }


    override suspend fun initializeSignContractRequest(request: InitializeRequestRequest): BaseResponse<Any> {

        return network.apiRequest { mainApi.initializeSignContractRequest(request) }

    }

    override suspend fun getSignContractSteps(): BaseResponse<Any> {
        return network.apiRequest { mainApi.getSignContractSteps() }
    }
}






