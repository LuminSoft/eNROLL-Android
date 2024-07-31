package com.luminsoft.enroll_sdk.main.main_domain.repository

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.StepModel
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestResponse

interface MainRepository {
    suspend fun generateOnboardingSessionToken(request: GenerateOnboardingSessionTokenRequest): Either<SdkFailure, String>
    suspend fun getOnBoardingStepsConfigurations(): Either<SdkFailure, List<StepModel>>
    suspend fun initializeRequest(request: InitializeRequestRequest): Either<SdkFailure, InitializeRequestResponse>
}