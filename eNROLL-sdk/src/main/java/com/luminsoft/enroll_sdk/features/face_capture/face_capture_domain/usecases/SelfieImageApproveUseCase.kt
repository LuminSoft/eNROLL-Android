package com.luminsoft.enroll_sdk.features.face_capture.face_capture_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.SelfieImageApproveRequestModel
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_domain.repository.FaceCaptureRepository

class SelfieImageApproveUseCase(private val faceCaptureUseCase: FaceCaptureRepository) :
    UseCase<Either<SdkFailure, Null>, SelfieApproveParams> {

    override suspend fun call(params: SelfieApproveParams): Either<SdkFailure, Null> {
        val selfieImageApproveRequestModel = SelfieImageApproveRequestModel()

        return faceCaptureUseCase.selfieImageApprove(selfieImageApproveRequestModel)
    }
}
data class SelfieApproveParams(
    val customerId: String? = null,
)