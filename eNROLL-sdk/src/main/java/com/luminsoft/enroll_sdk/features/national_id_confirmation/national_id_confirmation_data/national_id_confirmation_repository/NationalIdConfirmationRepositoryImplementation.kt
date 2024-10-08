package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_repository


import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.NetworkFailure
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_approve.PersonalConfirmationApproveRequest
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.CustomerData
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.NationalIDConfirmationResponse
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.PersonalConfirmationUploadImageRequest
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_remote_data_source.NationalIdConfirmationRemoteDataSource
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.repository.NationalIdConfirmationRepository

class NationalIdConfirmationRepositoryImplementation(private val nationalIdConfirmationRemoteDataSource: NationalIdConfirmationRemoteDataSource) :
    NationalIdConfirmationRepository {

    override suspend fun personalConfirmationUploadImage(request: PersonalConfirmationUploadImageRequest): Either<SdkFailure, CustomerData> {
        return when (val response =
            nationalIdConfirmationRemoteDataSource.personalConfirmationUploadImage(request)) {
            is BaseResponse.Success -> {
                val nationalIDConfirmationResponse = response.data as NationalIDConfirmationResponse
                if (nationalIDConfirmationResponse.isSuccess!!) {
                    if (request.scanType == ScanType.PASSPORT)
                        Either.Right(nationalIDConfirmationResponse.passportData!!)
                    else
                        Either.Right(nationalIDConfirmationResponse.customerData!!)
                } else {
                    Either.Left(NetworkFailure(mes = nationalIDConfirmationResponse.message!!))
                }
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun personalConfirmationApprove(request: PersonalConfirmationApproveRequest): Either<SdkFailure, Null> {
        return when (val response =
            nationalIdConfirmationRemoteDataSource.personalConfirmationApprove(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }
}

