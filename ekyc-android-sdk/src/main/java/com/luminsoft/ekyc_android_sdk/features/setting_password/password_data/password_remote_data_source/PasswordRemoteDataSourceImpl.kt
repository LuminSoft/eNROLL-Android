package com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_remote_data_source
import com.luminsoft.ekyc_android_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_api.PasswordApi


class  PasswordRemoteDataSourceImpl (private val network:BaseRemoteDataSource, private val passwordApi: PasswordApi):
    PasswordRemoteDataSource {

    override suspend fun getCards(request: GetCardsRequest, ): BaseResponse<Any> {

            return network.apiRequest { passwordApi.getCards(request) }

    }
}






