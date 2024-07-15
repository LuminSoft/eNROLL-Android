package com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_data.face_capture_auth_api

import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.BasicResponseModel
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_data.face_capture_auth_models.UploadSelfieAuthRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FaceCaptureAuthApi {
    @POST("api/v1/authentication/UserFaceAuthentication/CheckUserFace")
    suspend fun uploadSelfieAuth(@Body request: UploadSelfieAuthRequestModel): Response<BasicResponseModel>
}