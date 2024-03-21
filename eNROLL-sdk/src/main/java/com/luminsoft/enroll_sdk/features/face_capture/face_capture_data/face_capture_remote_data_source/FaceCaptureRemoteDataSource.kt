package com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.SelfieImageApproveRequestModel
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.UploadSelfieRequestModel

interface  FaceCaptureRemoteDataSource  {
    suspend fun uploadSelfie(request: UploadSelfieRequestModel): BaseResponse<Any>

    suspend fun selfieImageApprove(request: SelfieImageApproveRequestModel): BaseResponse<Any>

}