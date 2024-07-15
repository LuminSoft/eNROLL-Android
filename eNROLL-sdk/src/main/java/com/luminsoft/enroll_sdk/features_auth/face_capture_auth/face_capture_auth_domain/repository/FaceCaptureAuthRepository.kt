package com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_data.face_capture_auth_models.UploadSelfieAuthRequestModel

interface FaceCaptureAuthRepository {
    suspend fun faceCaptureUploadSelfieAuth(request: UploadSelfieAuthRequestModel): Either<SdkFailure, Null>
}