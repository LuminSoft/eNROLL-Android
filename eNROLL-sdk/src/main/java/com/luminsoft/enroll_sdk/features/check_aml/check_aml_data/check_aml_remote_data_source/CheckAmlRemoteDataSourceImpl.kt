package com.luminsoft.enroll_sdk.features.check_aml.check_aml_data.check_aml_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.check_aml.check_aml_data.check_aml_api.CheckAmlApi


class CheckAmlRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val checkAmlApi: CheckAmlApi
) :
    CheckAmlRemoteDataSource {

    override suspend fun checkAml(): BaseResponse<Any> {
        return network.apiRequest { checkAmlApi.checkAml() }
    }
}






