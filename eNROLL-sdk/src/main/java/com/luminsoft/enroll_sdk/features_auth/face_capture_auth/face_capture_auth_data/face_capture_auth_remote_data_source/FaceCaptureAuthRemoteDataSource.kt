package com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_data.face_capture_auth_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_data.face_capture_auth_models.UploadSelfieAuthRequestModel


interface FaceCaptureAuthRemoteDataSource {
    suspend fun uploadSelfieAuth(request: UploadSelfieAuthRequestModel): BaseResponse<Any>
}