package com.luminsoft.enroll_sdk.features.face_capture.face_capture_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.SelfieImageApproveRequestModel
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.UploadSelfieData
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.UploadSelfieRequestModel

interface FaceCaptureRepository {
     suspend fun faceCaptureUploadSelfie(request: UploadSelfieRequestModel): Either<SdkFailure, UploadSelfieData>
     suspend fun selfieImageApprove(request: SelfieImageApproveRequestModel): Either<SdkFailure, Null>

}