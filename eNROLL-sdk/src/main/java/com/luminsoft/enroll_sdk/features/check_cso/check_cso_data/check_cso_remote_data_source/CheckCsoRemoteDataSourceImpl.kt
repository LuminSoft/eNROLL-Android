package com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_api.CheckCsoApi

class CheckCsoRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val checkCsoApi: CheckCsoApi
) : CheckCsoRemoteDataSource {
    override suspend fun checkCso(): BaseResponse<Any> {
        return network.apiRequest { checkCsoApi.checkCso() }
    }
}
