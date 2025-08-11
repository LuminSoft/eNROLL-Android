package com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_api

import ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_models.SignContractSendOTPResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignContractApi {
    @POST("api/v1/LowRiskDigitalSignature/SendOtp")
    suspend fun sendSignContractOtp(): Response<SignContractSendOTPResponseModel>

    @POST("api/v1/LowRiskDigitalSignature/ValidateOtpAndSignFile")
    suspend fun validateOTPSignContract(@Body request: ValidateOTPRequestModel): Response<BasicResponseModel>

}