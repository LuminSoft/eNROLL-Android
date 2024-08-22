package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_update_validate.PhoneUpdateValidatePhoneRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.repository.PhoneRepositoryUpdate


class ValidateOtpPhoneUpdateUseCase(private val phonesRepository: PhoneRepositoryUpdate) :
    UseCase<Either<SdkFailure, Null>, ValidateOtpPhoneUpdateUseCaseParams> {

    override suspend fun call(params: ValidateOtpPhoneUpdateUseCaseParams): Either<SdkFailure, Null> {
        val phoneUpdateRequestModel = PhoneUpdateValidatePhoneRequestModel()
        phoneUpdateRequestModel.otp = params.otp
        phoneUpdateRequestModel.oldPhoneNumber = params.oldPhone
        phoneUpdateRequestModel.id = params.id
        return phonesRepository.validateOTPUpdate(phoneUpdateRequestModel)
    }
}

data class ValidateOtpPhoneUpdateUseCaseParams(
    val id: Int? = null,
    val otp: String? = null,
    val oldPhone: String? = null
)
