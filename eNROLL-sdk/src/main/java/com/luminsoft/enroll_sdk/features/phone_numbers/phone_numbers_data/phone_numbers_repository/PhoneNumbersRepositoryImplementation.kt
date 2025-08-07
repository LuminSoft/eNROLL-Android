package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_repository


import ValidateOTPRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.countries_code.GetCountriesResponseModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.phone_info.PhoneInfoRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.verified_phones.GetVerifiedPhonesResponseModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_remote_data_source.PhoneNumbersRemoteDataSource
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.repository.PhoneNumbersRepository

class PhoneNumbersRepositoryImplementation(private val phoneNumbersRemoteDataSource: PhoneNumbersRemoteDataSource) :
    PhoneNumbersRepository {

    override suspend fun getCountries(): Either<SdkFailure, List<GetCountriesResponseModel>> {
        return when (val response = phoneNumbersRemoteDataSource.getCountries()) {
            is BaseResponse.Success -> {
                Either.Right((response.data as List<GetCountriesResponseModel>))
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun phoneInfo(request: PhoneInfoRequestModel): Either<SdkFailure, Null> {
        return when (val response = phoneNumbersRemoteDataSource.phoneInfo(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun sendOTP(): Either<SdkFailure, Null> {
        return when (val response = phoneNumbersRemoteDataSource.sendOTP()) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun approvePhones(): Either<SdkFailure, Null> {
        return when (val response = phoneNumbersRemoteDataSource.approvePhones()) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun validateOTP(request: ValidateOTPRequestModel): Either<SdkFailure, Null> {
        return when (val response = phoneNumbersRemoteDataSource.validateOTP(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun getVerifiedPhones(): Either<SdkFailure, List<GetVerifiedPhonesResponseModel>> {
        return when (val response = phoneNumbersRemoteDataSource.getVerifiedPhones()) {
            is BaseResponse.Success -> {
                Either.Right((response.data as List<GetVerifiedPhonesResponseModel>))
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun makeDefault(request: MakeDefaultRequestModel): Either<SdkFailure, Null> {
        return when (val response = phoneNumbersRemoteDataSource.makeDefault(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun deletePhone(request: MakeDefaultRequestModel): Either<SdkFailure, Null> {
        return when (val response = phoneNumbersRemoteDataSource.deletePhone(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

