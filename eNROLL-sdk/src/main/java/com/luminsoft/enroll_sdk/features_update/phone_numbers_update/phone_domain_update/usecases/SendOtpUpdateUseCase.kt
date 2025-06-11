package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.repository.PhoneRepositoryUpdate

class SendOtpUpdateUseCase(private val phonesRepository: PhoneRepositoryUpdate) :
    UseCase<Either<SdkFailure, Null>, Int> {

    override suspend fun call(params: Int): Either<SdkFailure, Null> {
        return phonesRepository.sendOTPUpdate(params)
    }
}



