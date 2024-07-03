package com.luminsoft.enroll_sdk.main_auth.main_auth_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main_auth.main_auth_domain.repository.MainAuthRepository


class GenerateAuthSessionTokenUsecase(private val mainRepository: MainAuthRepository) :
    UseCase<Either<SdkFailure, String>, GenerateAuthSessionTokenUsecaseParams> {

    override suspend fun call(params: GenerateAuthSessionTokenUsecaseParams): Either<SdkFailure, String> {

        val generateOnboardingSessionTokenRequest = GenerateOnboardingSessionTokenRequest()
        generateOnboardingSessionTokenRequest.tenantId = params.tenantId
        generateOnboardingSessionTokenRequest.tenantSecret = params.tenantSecret
        generateOnboardingSessionTokenRequest.applicantId = params.applicantId
        generateOnboardingSessionTokenRequest.levelOfTrustToken = params.levelOfTrustToken
        return mainRepository.generateAuthSessionToken(generateOnboardingSessionTokenRequest)
    }
}

data class GenerateAuthSessionTokenUsecaseParams(
    val tenantId: String,
    val tenantSecret: String,
    val applicantId: String,
    val levelOfTrustToken: String,
)