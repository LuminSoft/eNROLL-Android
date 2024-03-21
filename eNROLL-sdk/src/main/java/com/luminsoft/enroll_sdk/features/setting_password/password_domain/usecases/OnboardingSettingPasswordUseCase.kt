package com.luminsoft.enroll_sdk.features.setting_password.password_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_models.get_token.SetPasswordRequest
import com.luminsoft.enroll_sdk.features.setting_password.password_domain.repository.PasswordRepository

class OnboardingSettingPasswordUseCase(private val passwordRepository: PasswordRepository) :
    UseCase<Either<SdkFailure, Null>, PasswordUseCaseParams> {

    override suspend fun call(params: PasswordUseCaseParams): Either<SdkFailure, Null> {
        val setPasswordRequest = SetPasswordRequest()
        setPasswordRequest.password = params.password
        return passwordRepository.setPassword(setPasswordRequest)
    }
}

data class PasswordUseCaseParams(
    val password: String,
)