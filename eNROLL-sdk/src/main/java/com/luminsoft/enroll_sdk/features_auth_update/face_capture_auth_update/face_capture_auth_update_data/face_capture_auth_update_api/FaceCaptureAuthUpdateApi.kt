package com.luminsoft.enroll_sdk.features_auth_update.face_capture_auth_update.face_capture_auth_update_data.face_capture_auth_update_api

import UploadSelfieAuthUpdateRequestModel
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FaceCaptureAuthUpdateApi {
    @POST("api/v1/update/UserFaceUpdateAuthentication/CheckUserFace")
    suspend fun uploadSelfieAuthUpdate(@Body request: UploadSelfieAuthUpdateRequestModel): Response<BasicResponseModel>
}