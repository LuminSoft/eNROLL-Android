package com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_remote_data_source

import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_models.get_token.SetPasswordRequest

interface  PasswordRemoteDataSource  {
    suspend fun setPassword(request: SetPasswordRequest): BaseResponse<Any>
}