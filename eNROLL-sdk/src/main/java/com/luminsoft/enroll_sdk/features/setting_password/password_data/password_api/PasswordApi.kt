package com.luminsoft.enroll_sdk.features.setting_password.password_data.password_api

import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_models.get_token.SetPasswordRequest

import retrofit2.Response
import retrofit2.http.*


interface PasswordApi {
    @POST("api/v1/onboarding/PasswordInfo")
    suspend fun setPassword(@Body request: SetPasswordRequest): Response<String>
}