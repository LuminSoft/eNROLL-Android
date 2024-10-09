package com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_data.phone_auth_api

import ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PhoneAuthApi {
    @GET("api/v1/authentication/PhoneOtpAuthentication/SendPhoneOtp")
    suspend fun sendPhoneAuthOtp(): Response<BasicResponseModel>

    @POST("api/v1/authentication/PhoneOtpAuthentication/VerifyPhoneOtp")
    suspend fun validateOTPPhoneAuth(@Body request: ValidateOTPRequestModel): Response<BasicResponseModel>

}