package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_remote_data_source

import ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.phone_info.PhoneInfoRequestModel

interface PhoneNumbersRemoteDataSource {
    suspend fun getCountries(): BaseResponse<Any>
    suspend fun phoneInfo(request: PhoneInfoRequestModel): BaseResponse<Any>
    suspend fun sendOTP(): BaseResponse<Any>
    suspend fun approvePhones(): BaseResponse<Any>
    suspend fun validateOTP(request: ValidateOTPRequestModel): BaseResponse<Any>
    suspend fun getVerifiedPhones(): BaseResponse<Any>
    suspend fun makeDefault(request: MakeDefaultRequestModel): BaseResponse<Any>
}