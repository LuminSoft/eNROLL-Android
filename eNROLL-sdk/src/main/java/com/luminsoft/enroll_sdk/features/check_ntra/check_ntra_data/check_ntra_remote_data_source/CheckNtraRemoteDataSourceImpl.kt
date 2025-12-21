package com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_api.CheckNtraApi


class CheckNtraRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val checkNtraApi: CheckNtraApi
) :
    CheckNtraRemoteDataSource {

    override suspend fun checkNtra(): BaseResponse<Any> {
        return network.apiRequest { checkNtraApi.checkNtra() }
    }
}
