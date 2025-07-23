package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_api

import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_models.GetCurrentContractRequestModel
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_models.ValidateOTPLowRiskFRARequestModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming

interface LowRiskFRAApi {
    @GET("api/v1/authentication/PhoneOtpAuthentication/SendPhoneOtp")
    suspend fun sendLowRiskFRAOtp(): Response<BasicResponseModel>

    @POST("api/v1/authentication/PhoneOtpAuthentication/VerifyPhoneOtp")
    suspend fun validateOTPLowRiskFRA(@Body request: ValidateOTPLowRiskFRARequestModel): Response<BasicResponseModel>

    @Streaming
    @POST("api/v1/SignContractRequest/GetCurrentTextTemplate")
    suspend fun getCurrentContract(@Body request: GetCurrentContractRequestModel): Response<ResponseBody>

    @Streaming
    @GET("api/v1/SignContractRequest/GetSignContractFile")
    suspend fun getSignContractFile(): Response<ResponseBody>


}