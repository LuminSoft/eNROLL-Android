package com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_data.phone_auth_api

import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.BasicResponseModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.validate_otp.ValidateOTPRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PhoneAuthApi {
    @GET("/api/v1/authentication/PhoneOtpAuthentication/SendPhoneOtp")
    suspend fun sendPhoneAuthOtp(): Response<BasicResponseModel>

    @POST("/api/v1/authentication/PhoneOtpAuthentication/VerifyPhoneOtp")
    suspend fun validateOTPPhoneAuth(@Body request: ValidateOTPRequestModel): Response<BasicResponseModel>

}