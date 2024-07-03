package com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_data.password_auth_api

import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.BasicResponseModel
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_models.get_token.SetPasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PasswordAuthApi {
    @POST("/api/v1/authentication/PasswordAuthentication/VerifyPassword")
    suspend fun verifyPassword(@Body request: SetPasswordRequest): Response<BasicResponseModel>
}