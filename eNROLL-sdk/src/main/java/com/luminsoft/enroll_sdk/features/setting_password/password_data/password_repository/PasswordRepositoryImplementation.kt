package com.luminsoft.enroll_sdk.features.setting_password.password_data.password_repository


import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.setting_password.password_domain.repository.PasswordRepository
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_models.get_token.SetPasswordRequest
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_remote_data_source.PasswordRemoteDataSource

class PasswordRepositoryImplementation(private val passwordRemoteDataSource: PasswordRemoteDataSource) :
    PasswordRepository {
    override suspend fun setPassword(request: SetPasswordRequest): Either<SdkFailure, Null> {
        return when (val response = passwordRemoteDataSource.setPassword(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

