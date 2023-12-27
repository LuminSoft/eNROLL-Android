package com.luminsoft.ekyc_android_sdk.main.main_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.get_onboaring_configurations.StepModel
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest

interface MainRepository {
     suspend fun generateOnboardingSessionToken (request: GenerateOnboardingSessionTokenRequest): Either<SdkFailure, String>
     suspend fun getOnBoardingStepsConfigurations (): Either<SdkFailure, List<StepModel>>
     suspend fun initializeRequest(request: InitializeRequestRequest): Either<SdkFailure, Null>
}