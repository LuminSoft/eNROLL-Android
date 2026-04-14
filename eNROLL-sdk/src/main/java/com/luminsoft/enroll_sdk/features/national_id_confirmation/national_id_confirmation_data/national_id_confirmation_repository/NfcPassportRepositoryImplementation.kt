package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_repository

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.NetworkFailure
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.CustomerData
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.NationalIDConfirmationResponse
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.passport_nfc_upload.FailingPassportRequest
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.passport_nfc_upload.PassportNfcUploadRequest
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_remote_data_source.NfcPassportRemoteDataSource
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.repository.NfcPassportRepository

class NfcPassportRepositoryImplementation(
    private val nfcPassportRemoteDataSource: NfcPassportRemoteDataSource,
) : NfcPassportRepository {

    override suspend fun uploadPassportNfcData(request: PassportNfcUploadRequest): Either<SdkFailure, CustomerData> {
        return when (val response = nfcPassportRemoteDataSource.uploadPassportNfcData(request)) {
            is BaseResponse.Success -> {
                val confirmationResponse = response.data as? NationalIDConfirmationResponse
                    ?: return Either.Left(
                        NetworkFailure(
                            mes = "Invalid response type from server",
                            errorCode = null
                        )
                    )
                
                if (confirmationResponse.isSuccess == true) {
                    confirmationResponse.passportData?.let { data ->
                        Either.Right(data)
                    } ?: Either.Left(
                        NetworkFailure(
                            mes = "Missing passport data in response",
                            errorCode = null
                        )
                    )
                } else {
                    Either.Left(
                        NetworkFailure(
                            mes = confirmationResponse.message ?: "Unknown error",
                            errorCode = confirmationResponse.errorCode?.toInt()
                        )
                    )
                }
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun reportFailingPassport(request: FailingPassportRequest): Either<SdkFailure, Unit> {
        return when (val response = nfcPassportRemoteDataSource.reportFailingPassport(request)) {
            is BaseResponse.Success -> Either.Right(Unit)
            is BaseResponse.Error -> Either.Left(response.error)
        }
    }
}
