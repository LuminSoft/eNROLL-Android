package com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_data.phone_auth_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.validate_otp.ValidateOTPRequestModel

interface PhoneAuthRemoteDataSource {
    suspend fun sendPhoneAuthOtp(): BaseResponse<Any>
    suspend fun validateOTPPhoneAuth(request: ValidateOTPRequestModel): BaseResponse<Any>

}