package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_auth_configurations.StepSignContractModel

interface MainSignContractRepository {
    suspend fun generateSignContractSessionToken(request: GenerateOnboardingSessionTokenRequest): Either<SdkFailure, String>
    suspend fun getSignContractStepsConfigurations(): Either<SdkFailure, List<StepSignContractModel>>
    suspend fun initializeSignContractRequest(request: InitializeRequestRequest): Either<SdkFailure, Null>
}