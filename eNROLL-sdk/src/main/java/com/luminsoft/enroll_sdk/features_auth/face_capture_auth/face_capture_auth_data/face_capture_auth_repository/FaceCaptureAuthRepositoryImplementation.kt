package com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_data.face_capture_auth_repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_data.face_capture_auth_models.UploadSelfieAuthRequestModel
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_data.face_capture_auth_remote_data_source.FaceCaptureAuthRemoteDataSource
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_domain.repository.FaceCaptureAuthRepository

class FaceCaptureAuthRepositoryImplementation(private val faceCaptureRemoteDataSource: FaceCaptureAuthRemoteDataSource) :
    FaceCaptureAuthRepository {

    override suspend fun faceCaptureUploadSelfieAuth(request: UploadSelfieAuthRequestModel): Either<SdkFailure, Null> {
        return when (val response = faceCaptureRemoteDataSource.uploadSelfieAuth(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }
}

