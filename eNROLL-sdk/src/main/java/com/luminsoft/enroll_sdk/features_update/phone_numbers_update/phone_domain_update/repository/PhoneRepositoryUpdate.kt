package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.verified_phones.GetVerifiedPhonesResponseModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_add_new_update.PhoneUpdateAddNewResponseModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_info_update.PhoneUpdateRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_update_old_phone.PhoneUpdateOldPhoneRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_update_validate.PhoneUpdateValidatePhoneRequestModel

interface PhoneRepositoryUpdate {
    suspend fun updatePhoneAdd(request: PhoneUpdateRequestModel): Either<SdkFailure, PhoneUpdateAddNewResponseModel>
    suspend fun sendVerifyPhoneOtp(request: PhoneUpdateRequestModel): Either<SdkFailure, Null>
    suspend fun updateOldPhone(request: PhoneUpdateOldPhoneRequestModel): Either<SdkFailure, PhoneUpdateAddNewResponseModel>
    suspend fun sendOTPUpdate(id: Int): Either<SdkFailure, Null>
    suspend fun validateOTPUpdate(request: PhoneUpdateValidatePhoneRequestModel): Either<SdkFailure, Null>
    suspend fun getApplicantPhones(): Either<SdkFailure, List<GetVerifiedPhonesResponseModel>>
    suspend fun deletePhone(request: MakeDefaultRequestModel): Either<SdkFailure, Null>
    suspend fun makeDefault(request: MakeDefaultRequestModel): Either<SdkFailure, Null>
}