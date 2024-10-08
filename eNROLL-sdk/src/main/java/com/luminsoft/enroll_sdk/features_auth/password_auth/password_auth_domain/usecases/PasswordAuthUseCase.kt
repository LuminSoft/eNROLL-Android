package com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_models.get_token.SetPasswordRequest
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_domain.repository.PasswordAuthRepository

class PasswordAuthUseCase(private val passwordRepository: PasswordAuthRepository) :
    UseCase<Either<SdkFailure, Null>, PasswordAuthUseCaseParams> {

    override suspend fun call(params: PasswordAuthUseCaseParams): Either<SdkFailure, Null> {
        val setPasswordRequest = SetPasswordRequest()
        setPasswordRequest.password = params.password
        return passwordRepository.verifyPassword(setPasswordRequest)
    }
}

data class PasswordAuthUseCaseParams(
    val password: String,
)