package com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.NetworkFailure
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.SelfieImageApproveRequestModel
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.UploadSelfieData
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.UploadSelfieRequestModel
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.UploadSelfieResponseModel
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_domain.repository.FaceCaptureRepository
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_remote_data_source.FaceCaptureRemoteDataSource

class FaceCaptureRepositoryImplementation(private val faceCaptureRemoteDataSource: FaceCaptureRemoteDataSource) :
    FaceCaptureRepository {

    override suspend fun faceCaptureUploadSelfie(request: UploadSelfieRequestModel): Either<SdkFailure, UploadSelfieData> {
        return when (val response = faceCaptureRemoteDataSource.uploadSelfie(request)) {
            is BaseResponse.Success -> {
                val response = response.data as UploadSelfieResponseModel
                if (response.isSuccess!!) {
                    Either.Right(response.uploadSelfieData!!)
                } else {
                    Either.Left(NetworkFailure(mes = response.message!!))
                }
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }


    override suspend fun selfieImageApprove(request: SelfieImageApproveRequestModel): Either<SdkFailure, Null> {
        return when (val response =
            faceCaptureRemoteDataSource.selfieImageApprove(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

}

