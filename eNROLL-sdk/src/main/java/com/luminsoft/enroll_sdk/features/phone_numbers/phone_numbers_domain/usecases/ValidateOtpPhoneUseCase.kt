package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.validate_otp.ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.repository.PhoneNumbersRepository

class ValidateOtpPhoneUseCase(private val phoneNumbersRepository: PhoneNumbersRepository) :
    UseCase<Either<SdkFailure, Null>, ValidateOtpPhoneUseCaseParams> {

    override suspend fun call(params: ValidateOtpPhoneUseCaseParams): Either<SdkFailure, Null> {
        val validateOTPRequestModel = ValidateOTPRequestModel()
        validateOTPRequestModel.otp = params.otp
        return phoneNumbersRepository.validateOTP(validateOTPRequestModel)
    }
}

data class ValidateOtpPhoneUseCaseParams(
    val otp: String? = null
)
