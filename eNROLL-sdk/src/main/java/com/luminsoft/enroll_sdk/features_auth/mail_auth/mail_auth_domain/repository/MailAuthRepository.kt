package com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.validate_otp.ValidateOTPRequestModel

interface MailAuthRepository {
    suspend fun sendMailAuthOtp(): Either<SdkFailure, Null>
    suspend fun validateOTPMailAuth(request: ValidateOTPRequestModel): Either<SdkFailure, Null>
}