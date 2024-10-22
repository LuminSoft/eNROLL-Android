package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.MakeDefaultPhoneUseCaseParams
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.repository.PhoneRepositoryUpdate

class MakeDefaultPhoneUpdateUseCase(private val mailsRepository: PhoneRepositoryUpdate) :
    UseCase<Either<SdkFailure, Null>, MakeDefaultPhoneUseCaseParams> {

    override suspend fun call(params: MakeDefaultPhoneUseCaseParams): Either<SdkFailure, Null> {
        val makeDefaultRequestModel = MakeDefaultRequestModel()
        makeDefaultRequestModel.phoneNumber = params.phoneInfoId
        return mailsRepository.makeDefault(makeDefaultRequestModel)
    }
}

