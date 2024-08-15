package com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.verified_mails.GetVerifiedMailsResponseModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_add_new_update.MailUpdateAddNewResponseModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_info_update.MailUpdateRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_update_old_mail.MailUpdateOldMailRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_update_validate.MailUpdateValidateMailRequestModel

interface EmailRepositoryUpdate {
    suspend fun updateMailAdd(request: MailUpdateRequestModel): Either<SdkFailure, MailUpdateAddNewResponseModel>
    suspend fun sendVerifyEmailOtp(request: MailUpdateRequestModel): Either<SdkFailure, Null>
    suspend fun updateOldMail(request: MailUpdateOldMailRequestModel): Either<SdkFailure, MailUpdateAddNewResponseModel>
    suspend fun sendOTPUpdate(): Either<SdkFailure, Null>
    suspend fun validateOTPUpdate(request: MailUpdateValidateMailRequestModel): Either<SdkFailure, Null>
    suspend fun getApplicantEmails(): Either<SdkFailure, List<GetVerifiedMailsResponseModel>>
    suspend fun deleteMail(request: MakeDefaultRequestModel): Either<SdkFailure, Null>
    suspend fun makeDefault(request: MakeDefaultRequestModel): Either<SdkFailure, Null>
}