package com.luminsoft.enroll_sdk.main.main_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_domain.repository.MainRepository


class GenerateOnboardingSessionTokenUsecase(private val mainRepository: MainRepository) :
    UseCase<Either<SdkFailure, String>, GenerateOnboardingSessionTokenUsecaseParams> {

    override suspend fun call(params: GenerateOnboardingSessionTokenUsecaseParams): Either<SdkFailure, String> {

        val generateOnboardingSessionTokenRequest = GenerateOnboardingSessionTokenRequest()
        generateOnboardingSessionTokenRequest.tenantId = params.tenantId
        generateOnboardingSessionTokenRequest.tenantSecret = params.tenantSecret
        generateOnboardingSessionTokenRequest.deviceId = params.deviceId
        return mainRepository.generateOnboardingSessionToken(generateOnboardingSessionTokenRequest)
    }
}

data class GenerateOnboardingSessionTokenUsecaseParams(
    val tenantId: String,
    val tenantSecret: String,
    val deviceId: String,
)