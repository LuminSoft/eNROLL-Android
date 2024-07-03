package com.luminsoft.enroll_sdk.main_auth.main_auth_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_models.get_auth_configurations.StepAuthModel

interface MainAuthRepository {
    suspend fun generateAuthSessionToken(request: GenerateOnboardingSessionTokenRequest): Either<SdkFailure, String>
    suspend fun getAuthStepsConfigurations(): Either<SdkFailure, List<StepAuthModel>>
    suspend fun initializeAuthRequest(request: InitializeRequestRequest): Either<SdkFailure, Null>
}