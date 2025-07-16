package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_api
import ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LowRiskFRAApi {
    @GET("api/v1/authentication/PhoneOtpAuthentication/SendPhoneOtp")
    suspend fun sendLowRiskFRAOtp(): Response<BasicResponseModel>

    @POST("api/v1/authentication/PhoneOtpAuthentication/VerifyPhoneOtp")
    suspend fun validateOTPLowRiskFRA(@Body request: ValidateOTPRequestModel): Response<BasicResponseModel>

}