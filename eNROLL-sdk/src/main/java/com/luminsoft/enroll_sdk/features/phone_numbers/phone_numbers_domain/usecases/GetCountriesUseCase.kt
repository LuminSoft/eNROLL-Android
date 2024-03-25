package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.countries_code.GetCountriesResponseModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.repository.PhoneNumbersRepository

class GetCountriesUseCase(private val phoneNumbersRepository: PhoneNumbersRepository) :
    UseCase<Either<SdkFailure, List<GetCountriesResponseModel>>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, List<GetCountriesResponseModel>> {
        return phoneNumbersRepository.getCountries()
    }
}
