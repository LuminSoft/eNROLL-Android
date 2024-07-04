package com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_domain.repository.PhoneAuthRepository

class PhoneAuthSendOTPUseCase(private val phoneRepository: PhoneAuthRepository) :
    UseCase<Either<SdkFailure, Null>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, Null> {
        return phoneRepository.sendPhoneAuthOtp()
    }
}

