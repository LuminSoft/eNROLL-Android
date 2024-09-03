package com.luminsoft.enroll_sdk.features.email.email_domain.repository

import ValidateOTPRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.mail_info.MailInfoRequestModel
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.verified_mails.GetVerifiedMailsResponseModel

interface EmailRepository {
     suspend fun mailInfo(request: MailInfoRequestModel): Either<SdkFailure, Null>
     suspend fun sendOTP(): Either<SdkFailure, Null>
     suspend fun approveMails(): Either<SdkFailure, Null>
     suspend fun validateOTP(request: ValidateOTPRequestModel): Either<SdkFailure, Null>
     suspend fun getVerifiedMails(): Either<SdkFailure, List<GetVerifiedMailsResponseModel>>
     suspend fun makeDefault(request: MakeDefaultRequestModel): Either<SdkFailure, Null>}