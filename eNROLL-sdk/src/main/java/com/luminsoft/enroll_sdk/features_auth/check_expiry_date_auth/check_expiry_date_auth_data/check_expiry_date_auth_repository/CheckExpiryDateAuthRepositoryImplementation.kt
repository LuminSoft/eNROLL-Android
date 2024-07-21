package com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_data.check_expiry_date_auth_repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_data.check_expiry_date_auth_remote_data_source.CheckExpiryDateAuthRemoteDataSource
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_domain.repository.CheckExpiryDateAuthRepository

class CheckExpiryDateAuthRepositoryImplementation(private val checkExpiryDateRemoteDataSource: CheckExpiryDateAuthRemoteDataSource) :
    CheckExpiryDateAuthRepository {

    override suspend fun checkExpiryDate(): Either<SdkFailure, Null> {
        return when (val response = checkExpiryDateRemoteDataSource.checkExpiryDateAuth()) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }
}

