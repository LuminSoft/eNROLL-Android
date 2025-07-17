package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest

interface MainSignContractRepository {
    suspend fun generateSignContractSessionToken(request: GenerateOnboardingSessionTokenRequest): Either<SdkFailure, String>
    suspend fun initializeSignContractRequest(request: InitializeRequestRequest): Either<SdkFailure, Null>
}