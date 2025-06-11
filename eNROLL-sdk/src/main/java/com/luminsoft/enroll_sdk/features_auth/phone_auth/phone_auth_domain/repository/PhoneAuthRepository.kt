package com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_domain.repository

import ValidateOTPRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface PhoneAuthRepository {
    suspend fun sendPhoneAuthOtp(): Either<SdkFailure, Null>
    suspend fun validateOTPPhoneAuth(request: ValidateOTPRequestModel): Either<SdkFailure, Null>
}