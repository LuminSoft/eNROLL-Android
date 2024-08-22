package com.luminsoft.enroll_sdk.features.check_aml.check_aml_data.check_aml_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface CheckAmlRemoteDataSource {
    suspend fun checkAml(): BaseResponse<Any>
}