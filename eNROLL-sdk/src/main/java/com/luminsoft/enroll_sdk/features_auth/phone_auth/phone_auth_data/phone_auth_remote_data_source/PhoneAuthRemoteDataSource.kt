package com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_data.phone_auth_remote_data_source

import ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface PhoneAuthRemoteDataSource {
    suspend fun sendPhoneAuthOtp(): BaseResponse<Any>
    suspend fun validateOTPPhoneAuth(request: ValidateOTPRequestModel): BaseResponse<Any>

}