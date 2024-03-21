package com.luminsoft.enroll_sdk.features.face_capture.face_capture_domain.usecases

import android.graphics.Bitmap
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.UploadSelfieData
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.UploadSelfieRequestModel
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_domain.repository.FaceCaptureRepository
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper

class FaceCaptureUseCase(private val faceCaptureRepository: FaceCaptureRepository) :
    UseCase<Either<SdkFailure, UploadSelfieData>, UploadSelfieUseCaseParams> {

    override suspend fun call(params: UploadSelfieUseCaseParams): Either<SdkFailure, UploadSelfieData> {
        val uploadSelfieRequestModel = UploadSelfieRequestModel()
        uploadSelfieRequestModel.image = DotHelper.bitmapToBase64(params.image)
        uploadSelfieRequestModel.customerId = params.customerId
        return faceCaptureRepository.faceCaptureUploadSelfie(
            uploadSelfieRequestModel
        )
    }
}

data class UploadSelfieUseCaseParams(
    val image: Bitmap,
    val customerId: String? = null,
)