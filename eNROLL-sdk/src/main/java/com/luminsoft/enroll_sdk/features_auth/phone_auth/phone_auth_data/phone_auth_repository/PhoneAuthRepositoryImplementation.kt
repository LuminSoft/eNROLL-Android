package com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_data.phone_auth_repository

import ValidateOTPRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_data.phone_auth_remote_data_source.PhoneAuthRemoteDataSource
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_domain.repository.PhoneAuthRepository

class PhoneAuthRepositoryImplementation(private val mailRemoteDataSource: PhoneAuthRemoteDataSource) :
    PhoneAuthRepository {
    override suspend fun sendPhoneAuthOtp(): Either<SdkFailure, Null> {
        return when (val response = mailRemoteDataSource.sendPhoneAuthOtp()) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun validateOTPPhoneAuth(request: ValidateOTPRequestModel): Either<SdkFailure, Null> {
        return when (val response = mailRemoteDataSource.validateOTPPhoneAuth(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

}

