package com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_api


import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.SelfieImageApproveRequestModel
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.UploadSelfieRequestModel
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.UploadSelfieResponseModel
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.BasicResponseModel

import retrofit2.Response
import retrofit2.http.*

interface FaceCaptureApi {
    @POST("api/v1/onboarding/BiometricTest/UploadSelfieImage")
    suspend fun uploadSelfie(@Body request: UploadSelfieRequestModel): Response<UploadSelfieResponseModel>

    @POST("api/v1/onboarding/BiometricTest/Approve")
    suspend fun selfieApprove(@Body request: SelfieImageApproveRequestModel): Response<BasicResponseModel>

}