package com.luminsoft.enroll_sdk.features_update.update_passport.update_passport_domain.usecases

import IsTranslationEnabledResponse
import UpdatePassportRepository
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class UpdatePassportIsTranslationStepEnabledUseCase(private val updatePassportRepository: UpdatePassportRepository) :
    UseCase<Either<SdkFailure, IsTranslationEnabledResponse>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, IsTranslationEnabledResponse> {
        return updatePassportRepository.isTranslationStepEnabled()
    }
}