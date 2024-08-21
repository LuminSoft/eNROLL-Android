package com.luminsoft.enroll_sdk.main_update.main_update_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main_update.main_update_domain.repository.MainUpdateRepository


class GenerateUpdateSessionTokenUsecase(private val mainRepository: MainUpdateRepository) :
    UseCase<Either<SdkFailure, String>, GenerateUpdateSessionTokenUsecaseParams> {

    override suspend fun call(params: GenerateUpdateSessionTokenUsecaseParams): Either<SdkFailure, String> {

        val generateOnboardingSessionTokenRequest = GenerateOnboardingSessionTokenRequest()
        generateOnboardingSessionTokenRequest.tenantId = params.tenantId
        generateOnboardingSessionTokenRequest.tenantSecret = params.tenantSecret
        generateOnboardingSessionTokenRequest.applicantId = params.applicantId
        generateOnboardingSessionTokenRequest.updateSteps = params.updateSteps
        return mainRepository.generateUpdateRequestSessionToken(
            generateOnboardingSessionTokenRequest
        )
    }
}

data class GenerateUpdateSessionTokenUsecaseParams(
    val tenantId: String,
    val tenantSecret: String,
    val applicantId: String,
    val updateSteps: ArrayList<String>,
)