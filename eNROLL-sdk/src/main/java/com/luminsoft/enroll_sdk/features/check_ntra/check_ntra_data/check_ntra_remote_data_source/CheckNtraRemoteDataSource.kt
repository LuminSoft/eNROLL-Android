package com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface CheckNtraRemoteDataSource {
    suspend fun checkNtra(): BaseResponse<Any>
}
