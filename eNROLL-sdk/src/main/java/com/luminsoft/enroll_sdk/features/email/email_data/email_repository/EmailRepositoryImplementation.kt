package com.luminsoft.enroll_sdk.features.email.email_data.email_repository


import ValidateOTPRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.mail_info.MailInfoRequestModel
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.verified_mails.GetVerifiedMailsResponseModel
import com.luminsoft.enroll_sdk.features.email.email_data.email_remote_data_source.EmailRemoteDataSource
import com.luminsoft.enroll_sdk.features.email.email_domain.repository.EmailRepository

class EmailRepositoryImplementation(private val emailRemoteDataSource: EmailRemoteDataSource) :
    EmailRepository {

    override suspend fun mailInfo(request: MailInfoRequestModel): Either<SdkFailure, Null> {
        return when (val response = emailRemoteDataSource.mailInfo(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun sendOTP(): Either<SdkFailure, Null> {
        return when (val response = emailRemoteDataSource.sendOTP()) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun approveMails(): Either<SdkFailure, Null> {
        return when (val response = emailRemoteDataSource.approveMails()) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun validateOTP(request: ValidateOTPRequestModel): Either<SdkFailure, Null> {
        return when (val response = emailRemoteDataSource.validateOTP(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun getVerifiedMails(): Either<SdkFailure, List<GetVerifiedMailsResponseModel>> {
        return when (val response = emailRemoteDataSource.getVerifiedMails()) {
            is BaseResponse.Success -> {
                Either.Right((response.data as List<GetVerifiedMailsResponseModel>))
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun makeDefault(request: MakeDefaultRequestModel): Either<SdkFailure, Null> {
        return when (val response = emailRemoteDataSource.makeDefault(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun deleteMail(request: MakeDefaultRequestModel): Either<SdkFailure, Null> {
        return when (val response = emailRemoteDataSource.deleteMail(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }
}

