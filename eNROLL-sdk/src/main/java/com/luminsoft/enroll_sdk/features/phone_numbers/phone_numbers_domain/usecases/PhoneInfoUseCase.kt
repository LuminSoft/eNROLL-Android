package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.phone_info.PhoneInfoRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.repository.PhoneNumbersRepository

class PhoneInfoUseCase(private val phoneNumbersRepository: PhoneNumbersRepository) :
    UseCase<Either<SdkFailure, Null>, PhoneInfoUseCaseParams> {

    override suspend fun call(params: PhoneInfoUseCaseParams): Either<SdkFailure, Null> {
        val phoneInfoRequestModel = PhoneInfoRequestModel()
        phoneInfoRequestModel.phoneNumber = params.phoneNumber
        phoneInfoRequestModel.code = params.code
        return phoneNumbersRepository.phoneInfo(phoneInfoRequestModel)
    }
}

data class PhoneInfoUseCaseParams(
    val phoneNumber: String? = null,
    val code: String? = null
)
