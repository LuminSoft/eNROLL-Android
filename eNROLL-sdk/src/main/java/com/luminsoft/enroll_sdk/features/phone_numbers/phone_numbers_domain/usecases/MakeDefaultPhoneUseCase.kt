package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.repository.PhoneNumbersRepository

class MakeDefaultPhoneUseCase(private val phoneNumbersRepository: PhoneNumbersRepository) :
    UseCase<Either<SdkFailure, Null>, MakeDefaultPhoneUseCaseParams> {

    override suspend fun call(params: MakeDefaultPhoneUseCaseParams): Either<SdkFailure, Null> {
        val makeDefaultRequestModel = MakeDefaultRequestModel()
        makeDefaultRequestModel.phoneNumber = params.phoneInfoId
        return phoneNumbersRepository.makeDefault(makeDefaultRequestModel)
    }
}

data class MakeDefaultPhoneUseCaseParams(
    val phoneInfoId: String? = null
)
