package com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_data.mail_auth_api

import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.validate_otp.ValidateOTPRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MailAuthApi {
    @GET("api/v1/authentication/EmailOtpAuthentication/SendEmailOtp")
    suspend fun sendMailAuthOtp(): Response<BasicResponseModel>

    @POST("api/v1/authentication/EmailOtpAuthentication/VerifyEmailOtp")
    suspend fun validateOTPMailAuth(@Body request: ValidateOTPRequestModel): Response<BasicResponseModel>

}