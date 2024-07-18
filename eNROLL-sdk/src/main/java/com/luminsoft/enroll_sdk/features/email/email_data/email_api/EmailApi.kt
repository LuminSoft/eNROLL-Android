package com.luminsoft.enroll_sdk.features.email.email_data.email_api
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.mail_info.MailInfoRequestModel
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.validate_otp.ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.verified_mails.GetVerifiedMailsResponseModel
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.BasicResponseModel

import retrofit2.Response
import retrofit2.http.*

interface EmailApi {

    @POST("api/v1/onboarding/EmailInfo")
    suspend fun mailInfo(@Body request: MailInfoRequestModel): Response<BasicResponseModel>

    @POST("api/v1/onboarding/EmailInfo/SendOtp")
    suspend fun sendOTP(): Response<BasicResponseModel>

    @POST("api/v1/onboarding/EmailInfo/Approve")
    suspend fun approveMails(): Response<BasicResponseModel>

    @POST("api/v1/onboarding/EmailInfo/ValidateOTP")
    suspend fun validateOTP(@Body request: ValidateOTPRequestModel): Response<BasicResponseModel>

    @GET("api/v1/onboarding/EmailInfo/GetVerifiedEmails")
    suspend fun getVerifiedMails(): Response<List<GetVerifiedMailsResponseModel>>

    @POST("api/v1/onboarding/EmailInfo/SetDefault")
    suspend fun makeDefault(@Body request: MakeDefaultRequestModel): Response<BasicResponseModel>

}