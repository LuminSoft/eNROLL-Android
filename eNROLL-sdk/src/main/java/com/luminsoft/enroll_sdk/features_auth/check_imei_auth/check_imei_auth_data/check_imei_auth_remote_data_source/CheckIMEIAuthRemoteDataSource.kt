package com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_data.check_imei_auth_remote_data_source

import CheckIMEIRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface CheckIMEIAuthRemoteDataSource {
    suspend fun checkIMEIAuth(request: CheckIMEIRequestModel): BaseResponse<Any>
}

