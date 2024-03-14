package com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_api


import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models.SelfieImageApproveRequestModel
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models.UploadSelfieRequestModel
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models.UploadSelfieResponseModel

import retrofit2.Response
import retrofit2.http.*

interface FaceCaptureApi {
    @POST("/api/v1/onboarding/BiometricTest/UploadSelfieImage")
    suspend fun uploadSelfie(@Body request: UploadSelfieRequestModel): Response<UploadSelfieResponseModel>

    @POST("/api/v1/onboarding/BiometricTest/Approve")
    suspend fun selfieApprove(@Body request: SelfieImageApproveRequestModel): Response<String>

}