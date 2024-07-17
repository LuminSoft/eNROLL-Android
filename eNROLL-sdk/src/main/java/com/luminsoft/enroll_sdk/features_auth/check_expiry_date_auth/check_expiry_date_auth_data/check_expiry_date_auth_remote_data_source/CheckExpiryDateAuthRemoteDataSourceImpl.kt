package com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_data.check_expiry_date_auth_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_data.check_expiry_date_auth_api.CheckExpiryDateAuthApi


class CheckExpiryDateAuthRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val checkExpiryDate: CheckExpiryDateAuthApi
) :
    CheckExpiryDateAuthRemoteDataSource {


    override suspend fun checkExpiryDateAuth(): BaseResponse<Any> {
        return network.apiRequest { checkExpiryDate.checkExpiryDateAuth() }
    }
}






