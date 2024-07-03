package com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_data.password_auth_repository


import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_models.get_token.SetPasswordRequest
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_data.password_auth_remote_data_source.PasswordAuthRemoteDataSource
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_domain.repository.PasswordAuthRepository

class PasswordAuthRepositoryImplementation(private val passwordRemoteDataSource: PasswordAuthRemoteDataSource) :
    PasswordAuthRepository {
    override suspend fun verifyPassword(request: SetPasswordRequest): Either<SdkFailure, Null> {
        return when (val response = passwordRemoteDataSource.verifyPassword(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

