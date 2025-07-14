package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_remote_data_source_update

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_info_update.PhoneUpdateRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_update_old_phone.PhoneUpdateOldPhoneRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_update_validate.PhoneUpdateValidatePhoneRequestModel

interface PhoneRemoteDataSourceUpdate {
    suspend fun updatePhoneAdd(request: PhoneUpdateRequestModel): BaseResponse<Any>
    suspend fun sendVerifyPhoneOtp(request: PhoneUpdateRequestModel): BaseResponse<Any>
    suspend fun updateOldPhone(request: PhoneUpdateOldPhoneRequestModel): BaseResponse<Any>
    suspend fun sendOTPUpdate(id: Int): BaseResponse<Any>
    suspend fun validateOTPUpdate(request: PhoneUpdateValidatePhoneRequestModel): BaseResponse<Any>
    suspend fun getApplicantPhones(): BaseResponse<Any>
    suspend fun deletePhone(request: MakeDefaultRequestModel): BaseResponse<Any>
    suspend fun makeDefault(request: MakeDefaultRequestModel): BaseResponse<Any>
}