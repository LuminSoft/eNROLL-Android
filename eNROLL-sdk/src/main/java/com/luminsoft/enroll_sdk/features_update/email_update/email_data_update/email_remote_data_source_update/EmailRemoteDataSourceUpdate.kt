package com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_remote_data_source_update

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_info_update.MailUpdateRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_update_old_mail.MailUpdateOldMailRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_update_validate.MailUpdateValidateMailRequestModel

interface EmailRemoteDataSourceUpdate {
    suspend fun updateMailAdd(request: MailUpdateRequestModel): BaseResponse<Any>
    suspend fun sendVerifyEmailOtp(request: MailUpdateRequestModel): BaseResponse<Any>
    suspend fun updateOldMail(request: MailUpdateOldMailRequestModel): BaseResponse<Any>
    suspend fun sendOTPUpdate(): BaseResponse<Any>
    suspend fun validateOTPUpdate(request: MailUpdateValidateMailRequestModel): BaseResponse<Any>
    suspend fun getApplicantEmails(): BaseResponse<Any>
    suspend fun deleteMail(request: MakeDefaultRequestModel): BaseResponse<Any>
    suspend fun makeDefault(request: MakeDefaultRequestModel): BaseResponse<Any>
}