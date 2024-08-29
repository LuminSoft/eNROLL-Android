package com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_data.mail_auth_repository


import ValidateOTPRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_data.mail_auth_remote_data_source.MailAuthRemoteDataSource
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_domain.repository.MailAuthRepository

class MailAuthRepositoryImplementation(private val mailRemoteDataSource: MailAuthRemoteDataSource) :
    MailAuthRepository {
    override suspend fun sendMailAuthOtp(): Either<SdkFailure, Null> {
        return when (val response = mailRemoteDataSource.sendMailAuthOtp()) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun validateOTPMailAuth(request: ValidateOTPRequestModel): Either<SdkFailure, Null> {
        return when (val response = mailRemoteDataSource.validateOTPMailAuth(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

}

