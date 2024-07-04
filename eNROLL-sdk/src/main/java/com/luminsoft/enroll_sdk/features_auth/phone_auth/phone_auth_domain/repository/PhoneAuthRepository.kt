package com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.validate_otp.ValidateOTPRequestModel

interface PhoneAuthRepository {
    suspend fun sendPhoneAuthOtp(): Either<SdkFailure, Null>
    suspend fun validateOTPPhoneAuth(request: ValidateOTPRequestModel): Either<SdkFailure, Null>
}