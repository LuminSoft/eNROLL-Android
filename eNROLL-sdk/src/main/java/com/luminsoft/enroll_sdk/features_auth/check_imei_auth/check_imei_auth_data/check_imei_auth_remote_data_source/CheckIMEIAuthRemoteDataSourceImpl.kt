package com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_data.check_imei_auth_remote_data_source

import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_data.imei_models.CheckIMEIRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_data.check_imei_auth_api.CheckIMEIAuthApi


class CheckIMEIAuthRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val checkIMEI: CheckIMEIAuthApi
) :
    CheckIMEIAuthRemoteDataSource {
    override suspend fun checkIMEIAuth(request: CheckIMEIRequestModel): BaseResponse<Any> {
        return network.apiRequest { checkIMEI.checkIMEIAuth(request) }
    }


}












