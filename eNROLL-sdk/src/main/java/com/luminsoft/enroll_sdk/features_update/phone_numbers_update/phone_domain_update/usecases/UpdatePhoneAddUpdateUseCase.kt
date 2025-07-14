package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_add_new_update.PhoneUpdateAddNewResponseModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_info_update.PhoneUpdateRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.repository.PhoneRepositoryUpdate

class UpdatePhoneAddUpdateUseCase(private val phonesRepository: PhoneRepositoryUpdate) :
    UseCase<Either<SdkFailure, PhoneUpdateAddNewResponseModel>, UpdatePhoneAddUseCaseParams> {

    override suspend fun call(params: UpdatePhoneAddUseCaseParams): Either<SdkFailure, PhoneUpdateAddNewResponseModel> {
        val phoneUpdateRequestModel = PhoneUpdateRequestModel()
        phoneUpdateRequestModel.phoneNumber = params.phone
        phoneUpdateRequestModel.code = params.code
        return phonesRepository.updatePhoneAdd(phoneUpdateRequestModel)
    }
}

data class UpdatePhoneAddUseCaseParams(
    val phone: String? = null,
    val code: String? = null
)

