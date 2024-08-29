package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_remote_data_source

import ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_api.PhoneNumbersApi
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.phone_info.PhoneInfoRequestModel

class PhoneNumbersRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val phoneNumbersApi: PhoneNumbersApi
) :
    PhoneNumbersRemoteDataSource {
    override suspend fun getCountries(): BaseResponse<Any> {
        return network.apiRequest { phoneNumbersApi.getCountries() }
    }

    override suspend fun phoneInfo(request: PhoneInfoRequestModel): BaseResponse<Any> {
        return network.apiRequest { phoneNumbersApi.phoneInfo(request) }
    }

    override suspend fun sendOTP(): BaseResponse<Any> {
        return network.apiRequest { phoneNumbersApi.sendOTP() }
    }

    override suspend fun approvePhones(): BaseResponse<Any> {
        return network.apiRequest { phoneNumbersApi.approvePhones() }
    }

    override suspend fun validateOTP(request: ValidateOTPRequestModel): BaseResponse<Any> {
        return network.apiRequest { phoneNumbersApi.validateOTP(request) }
    }

    override suspend fun getVerifiedPhones(): BaseResponse<Any> {
        return network.apiRequest { phoneNumbersApi.getVerifiedPhones() }
    }

    override suspend fun makeDefault(request: MakeDefaultRequestModel): BaseResponse<Any> {
        return network.apiRequest { phoneNumbersApi.makeDefault(request) }
    }


}






