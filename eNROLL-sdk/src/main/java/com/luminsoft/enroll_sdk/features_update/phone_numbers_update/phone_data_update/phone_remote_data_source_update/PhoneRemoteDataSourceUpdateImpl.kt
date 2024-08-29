package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_remote_data_source_update

import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_api_update.PhoneApiUpdate
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_info_update.PhoneUpdateRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_update_old_phone.PhoneUpdateOldPhoneRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_update_validate.PhoneUpdateValidatePhoneRequestModel


class PhoneRemoteDataSourceUpdateImpl(
    private val network: BaseRemoteDataSource,
    private val phoneApi: PhoneApiUpdate
) :
    PhoneRemoteDataSourceUpdate {
    override suspend fun updatePhoneAdd(request: PhoneUpdateRequestModel): BaseResponse<Any> {
        return network.apiRequest { phoneApi.updatePhoneAdd(request) }
    }

    override suspend fun sendVerifyPhoneOtp(request: PhoneUpdateRequestModel): BaseResponse<Any> {
        return network.apiRequest { phoneApi.sendVerifyPhoneOtp(request) }
    }

    override suspend fun sendOTPUpdate(id: Int): BaseResponse<Any> {
        return network.apiRequest { phoneApi.sendOTPUpdate(id) }
    }

    override suspend fun updateOldPhone(request: PhoneUpdateOldPhoneRequestModel): BaseResponse<Any> {
        return network.apiRequest { phoneApi.updateOldPhone(request) }
    }

    override suspend fun validateOTPUpdate(request: PhoneUpdateValidatePhoneRequestModel): BaseResponse<Any> {
        return network.apiRequest { phoneApi.validateOTPUpdate(request) }
    }

    override suspend fun getApplicantPhones(): BaseResponse<Any> {
        return network.apiRequest { phoneApi.getApplicantPhones() }
    }

    override suspend fun deletePhone(request: MakeDefaultRequestModel): BaseResponse<Any> {
        return network.apiRequest { phoneApi.deletePhone(request) }
    }

    override suspend fun makeDefault(request: MakeDefaultRequestModel): BaseResponse<Any> {
        return network.apiRequest { phoneApi.makeDefault(request) }
    }

}






