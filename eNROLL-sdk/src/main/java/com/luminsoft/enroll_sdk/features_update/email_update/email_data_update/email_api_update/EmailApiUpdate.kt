package com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_api_update

import com.luminsoft.enroll_sdk.features.email.email_data.email_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.verified_mails.GetVerifiedMailsResponseModel
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.BasicResponseModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_add_new_update.MailUpdateAddNewResponseModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_info_update.MailUpdateRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_update_old_mail.MailUpdateOldMailRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_update_validate.MailUpdateValidateMailRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EmailApiUpdate {

    @POST("api/v1/update/UpdateEmailInfo/Add")
    suspend fun updateMailAdd(@Body request: MailUpdateRequestModel): Response<MailUpdateAddNewResponseModel>

    @POST("api/v1/update/UpdateRequest/SendVerifyEmailOtp")
    suspend fun sendVerifyEmailOtp(@Body request: MailUpdateRequestModel): Response<BasicResponseModel>

    @POST("api/v1/update/UpdateEmailInfo/Update")
    suspend fun updateOldMail(@Body request: MailUpdateOldMailRequestModel): Response<MailUpdateAddNewResponseModel>

    @POST("api/v1/update/UpdateEmailInfo/SendOTP")
    suspend fun sendOTPUpdate(@Query("id") id: Int): Response<BasicResponseModel>

    @POST("api/v1/update/UpdateEmailInfo/ValidateOTP")
    suspend fun validateOTPUpdate(@Body request: MailUpdateValidateMailRequestModel): Response<BasicResponseModel>

    @GET("api/v1/update/UpdateEmailInfo/GetApplicantEmails")
    suspend fun getApplicantEmails(): Response<List<GetVerifiedMailsResponseModel>>

    @POST("api/v1/update/UpdateEmailInfo/Archieve")
    suspend fun deleteMail(@Body request: MakeDefaultRequestModel): Response<BasicResponseModel>

    @POST("api/v1/update/UpdateEmailInfo/MakeDefault")
    suspend fun makeDefault(@Body request: MakeDefaultRequestModel): Response<BasicResponseModel>

}