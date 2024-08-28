package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_info_update.PhoneUpdateRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.repository.PhoneRepositoryUpdate

class SendVerifyPhoneOtpUseCase(private val phonesRepository: PhoneRepositoryUpdate) :
    UseCase<Either<SdkFailure, Null>, UpdatePhoneAddUseCaseParams> {

    override suspend fun call(params: UpdatePhoneAddUseCaseParams): Either<SdkFailure, Null> {
        val phoneUpdateRequestModel = PhoneUpdateRequestModel()
        phoneUpdateRequestModel.phoneNumber = params.phone
        return phonesRepository.sendVerifyPhoneOtp(phoneUpdateRequestModel)
    }
}



