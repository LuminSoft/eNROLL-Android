package com.luminsoft.enroll_sdk.features.setting_password.password_data.password_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_models.get_token.SetPasswordRequest
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_api.PasswordApi


class PasswordRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val passwordApi: PasswordApi
) :
    PasswordRemoteDataSource {

    override suspend fun setPassword(request: SetPasswordRequest): BaseResponse<Any> {
        return network.apiRequest { passwordApi.setPassword(request) }
    }
}






