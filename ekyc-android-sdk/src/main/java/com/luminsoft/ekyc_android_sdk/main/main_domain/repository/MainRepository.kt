package com.luminsoft.ekyc_android_sdk.main.main_domain.repository

import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest

interface MainRepository {
     suspend fun generateOnboardingSessionToken (request: GenerateOnboardingSessionTokenRequest): Either<com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure, String>
}