package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_repository_update

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.verified_phones.GetVerifiedPhonesResponseModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_add_new_update.PhoneUpdateAddNewResponseModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_info_update.PhoneUpdateRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_update_old_phone.PhoneUpdateOldPhoneRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_update_validate.PhoneUpdateValidatePhoneRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_remote_data_source_update.PhoneRemoteDataSourceUpdate
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.repository.PhoneRepositoryUpdate

class PhoneRepositoryUpdateImplementation(private val phoneRemoteDataSource: PhoneRemoteDataSourceUpdate) :
    PhoneRepositoryUpdate {


    override suspend fun updatePhoneAdd(request: PhoneUpdateRequestModel): Either<SdkFailure, PhoneUpdateAddNewResponseModel> {
        return when (val response = phoneRemoteDataSource.updatePhoneAdd(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as PhoneUpdateAddNewResponseModel))
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun sendVerifyPhoneOtp(request: PhoneUpdateRequestModel): Either<SdkFailure, Null> {
        return when (val response = phoneRemoteDataSource.sendVerifyPhoneOtp(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun updateOldPhone(request: PhoneUpdateOldPhoneRequestModel): Either<SdkFailure, PhoneUpdateAddNewResponseModel> {
        return when (val response = phoneRemoteDataSource.updateOldPhone(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as PhoneUpdateAddNewResponseModel))
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun sendOTPUpdate(id: Int): Either<SdkFailure, Null> {
        return when (val response = phoneRemoteDataSource.sendOTPUpdate(id)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun validateOTPUpdate(request: PhoneUpdateValidatePhoneRequestModel): Either<SdkFailure, Null> {
        return when (val response = phoneRemoteDataSource.validateOTPUpdate(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun getApplicantPhones(): Either<SdkFailure, List<GetVerifiedPhonesResponseModel>> {
        return when (val response = phoneRemoteDataSource.getApplicantPhones()) {
            is BaseResponse.Success -> {
                Either.Right((response.data as List<GetVerifiedPhonesResponseModel>))
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun deletePhone(request: MakeDefaultRequestModel): Either<SdkFailure, Null> {
        return when (val response = phoneRemoteDataSource.deletePhone(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun makeDefault(request: MakeDefaultRequestModel): Either<SdkFailure, Null> {
        return when (val response = phoneRemoteDataSource.makeDefault(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

}

