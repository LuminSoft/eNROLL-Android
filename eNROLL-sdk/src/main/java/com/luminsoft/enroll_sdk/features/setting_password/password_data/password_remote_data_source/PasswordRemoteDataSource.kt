package com.luminsoft.enroll_sdk.features.setting_password.password_data.password_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_models.get_token.SetPasswordRequest

interface  PasswordRemoteDataSource  {
    suspend fun setPassword(request: SetPasswordRequest): BaseResponse<Any>
}