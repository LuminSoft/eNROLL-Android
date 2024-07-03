package com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_data.password_auth_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_models.get_token.SetPasswordRequest

interface PasswordAuthRemoteDataSource {
    suspend fun verifyPassword(request: SetPasswordRequest): BaseResponse<Any>
}