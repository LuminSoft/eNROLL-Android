package com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_domain.usecases

import android.graphics.Bitmap
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_data.face_capture_auth_models.UploadSelfieAuthRequestModel
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_domain.repository.FaceCaptureAuthRepository
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper

class FaceCaptureAuthUseCase(private val faceCaptureRepository: FaceCaptureAuthRepository) :
    UseCase<Either<SdkFailure, Null>, UploadSelfieAuthUseCaseParams> {

    override suspend fun call(params: UploadSelfieAuthUseCaseParams): Either<SdkFailure, Null> {
        val uploadSelfieRequestModel = UploadSelfieAuthRequestModel()
        uploadSelfieRequestModel.image = DotHelper.bitmapToBase64(params.image)
        return faceCaptureRepository.faceCaptureUploadSelfieAuth(
            uploadSelfieRequestModel
        )
    }
}

data class UploadSelfieAuthUseCaseParams(
    val image: Bitmap,
)