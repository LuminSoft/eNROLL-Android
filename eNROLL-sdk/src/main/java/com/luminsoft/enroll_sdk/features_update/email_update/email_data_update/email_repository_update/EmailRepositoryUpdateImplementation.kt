package com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_repository_update

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.verified_mails.GetVerifiedMailsResponseModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_add_new_update.MailUpdateAddNewResponseModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_info_update.MailUpdateRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_update_old_mail.MailUpdateOldMailRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_update_validate.MailUpdateValidateMailRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_remote_data_source_update.EmailRemoteDataSourceUpdate
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.repository.EmailRepositoryUpdate

class EmailRepositoryUpdateImplementation(private val emailRemoteDataSource: EmailRemoteDataSourceUpdate) :
    EmailRepositoryUpdate {


    override suspend fun updateMailAdd(request: MailUpdateRequestModel): Either<SdkFailure, MailUpdateAddNewResponseModel> {
        return when (val response = emailRemoteDataSource.updateMailAdd(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as MailUpdateAddNewResponseModel))
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun sendVerifyEmailOtp(request: MailUpdateRequestModel): Either<SdkFailure, Null> {
        return when (val response = emailRemoteDataSource.sendVerifyEmailOtp(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun updateOldMail(request: MailUpdateOldMailRequestModel): Either<SdkFailure, MailUpdateAddNewResponseModel> {
        return when (val response = emailRemoteDataSource.updateOldMail(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as MailUpdateAddNewResponseModel))
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun sendOTPUpdate(): Either<SdkFailure, Null> {
        return when (val response = emailRemoteDataSource.sendOTPUpdate()) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun validateOTPUpdate(request: MailUpdateValidateMailRequestModel): Either<SdkFailure, Null> {
        return when (val response = emailRemoteDataSource.validateOTPUpdate(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun getApplicantEmails(): Either<SdkFailure, List<GetVerifiedMailsResponseModel>> {
        return when (val response = emailRemoteDataSource.getApplicantEmails()) {
            is BaseResponse.Success -> {
                Either.Right((response.data as List<GetVerifiedMailsResponseModel>))
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

}

