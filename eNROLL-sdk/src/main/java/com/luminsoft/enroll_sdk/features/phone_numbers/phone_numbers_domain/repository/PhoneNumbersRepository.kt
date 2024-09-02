package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.repository

import ValidateOTPRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.countries_code.GetCountriesResponseModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.phone_info.PhoneInfoRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.verified_phones.GetVerifiedPhonesResponseModel

interface PhoneNumbersRepository {

    suspend fun getCountries(): Either<SdkFailure, List<GetCountriesResponseModel>>
    suspend fun phoneInfo(request: PhoneInfoRequestModel): Either<SdkFailure, Null>
    suspend fun sendOTP(): Either<SdkFailure, Null>
    suspend fun approvePhones(): Either<SdkFailure, Null>
    suspend fun validateOTP(request: ValidateOTPRequestModel): Either<SdkFailure, Null>
    suspend fun getVerifiedPhones(): Either<SdkFailure, List<GetVerifiedPhonesResponseModel>>
    suspend fun makeDefault(request: MakeDefaultRequestModel): Either<SdkFailure, Null>
}