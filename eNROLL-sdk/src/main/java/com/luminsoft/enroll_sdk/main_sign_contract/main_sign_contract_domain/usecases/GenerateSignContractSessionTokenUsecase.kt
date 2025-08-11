package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.repository.MainSignContractRepository


class GenerateSignContractSessionTokenUsecase(private val mainRepository: MainSignContractRepository) :
    UseCase<Either<SdkFailure, String>, GenerateSignContractSessionTokenUsecaseParams> {

    override suspend fun call(params: GenerateSignContractSessionTokenUsecaseParams): Either<SdkFailure, String> {

        val generateOnboardingSessionTokenRequest = GenerateOnboardingSessionTokenRequest()
        generateOnboardingSessionTokenRequest.tenantId = params.tenantId
        generateOnboardingSessionTokenRequest.tenantSecret = params.tenantSecret
        generateOnboardingSessionTokenRequest.applicantId = params.applicantId
        generateOnboardingSessionTokenRequest.contractTemplateId = params.contractTemplateId
        generateOnboardingSessionTokenRequest.contractParams = params.contractParams
        generateOnboardingSessionTokenRequest.signContractMode = "5"
        generateOnboardingSessionTokenRequest.signContractApproach = "1"
        return mainRepository.generateSignContractSessionToken(generateOnboardingSessionTokenRequest)
    }
}

data class GenerateSignContractSessionTokenUsecaseParams(
    val tenantId: String,
    val tenantSecret: String,
    val applicantId: String,
    val contractTemplateId: String,
    val contractParams: String
)