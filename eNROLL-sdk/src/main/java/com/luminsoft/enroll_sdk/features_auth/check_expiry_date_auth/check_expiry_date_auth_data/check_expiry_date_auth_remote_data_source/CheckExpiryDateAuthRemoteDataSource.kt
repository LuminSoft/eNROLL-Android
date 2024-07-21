package com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_data.check_expiry_date_auth_remote_data_source
import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface CheckExpiryDateAuthRemoteDataSource {
    suspend fun checkExpiryDateAuth(): BaseResponse<Any>
}