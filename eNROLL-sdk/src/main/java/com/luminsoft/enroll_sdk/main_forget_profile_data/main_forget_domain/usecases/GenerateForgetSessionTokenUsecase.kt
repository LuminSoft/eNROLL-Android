package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.repository.MainForgetRepository


class GenerateForgetSessionTokenUsecase(private val mainRepository: MainForgetRepository) :
    UseCase<Either<SdkFailure, String>, GenerateForgetSessionTokenUsecaseParams> {

    override suspend fun call(params: GenerateForgetSessionTokenUsecaseParams): Either<SdkFailure, String> {

        val generateOnboardingSessionTokenRequest = GenerateOnboardingSessionTokenRequest()
        generateOnboardingSessionTokenRequest.tenantId = params.tenantId
        generateOnboardingSessionTokenRequest.tenantSecret = params.tenantSecret
        return mainRepository.generateForgetRequestSessionToken(
            generateOnboardingSessionTokenRequest
        )
    }
}

data class GenerateForgetSessionTokenUsecaseParams(
    val tenantId: String,
    val tenantSecret: String,

    )