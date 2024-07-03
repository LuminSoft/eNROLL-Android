package com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_data.password_auth_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_models.get_token.SetPasswordRequest
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_data.password_auth_api.PasswordAuthApi


class PasswordAuthRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val passwordApi: PasswordAuthApi
) :
    PasswordAuthRemoteDataSource {
    override suspend fun verifyPassword(request: SetPasswordRequest): BaseResponse<Any> {
        return network.apiRequest { passwordApi.verifyPassword(request) }
    }
}






