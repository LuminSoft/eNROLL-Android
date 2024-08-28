package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_add_new_update.PhoneUpdateAddNewResponseModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_update_old_phone.PhoneUpdateOldPhoneRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.repository.PhoneRepositoryUpdate

class UpdateOldPhoneUpdateUseCase(private val phonesRepository: PhoneRepositoryUpdate) :
    UseCase<Either<SdkFailure, PhoneUpdateAddNewResponseModel>, UpdateOldPhoneUseCaseParams> {

    override suspend fun call(params: UpdateOldPhoneUseCaseParams): Either<SdkFailure, PhoneUpdateAddNewResponseModel> {
        val phoneUpdateRequestModel = PhoneUpdateOldPhoneRequestModel()
        phoneUpdateRequestModel.id = params.id
        phoneUpdateRequestModel.phoneNumber = params.updatedPhone
        return phonesRepository.updateOldPhone(phoneUpdateRequestModel)
    }
}

data class UpdateOldPhoneUseCaseParams(
    val id: Int? = null,
    val updatedPhone: String? = null
)

