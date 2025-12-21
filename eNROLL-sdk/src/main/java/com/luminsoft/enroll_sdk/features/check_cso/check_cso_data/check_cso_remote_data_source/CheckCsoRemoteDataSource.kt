package com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface CheckCsoRemoteDataSource {
    suspend fun checkCso(): BaseResponse<Any>
}
