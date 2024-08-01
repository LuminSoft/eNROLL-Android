package com.luminsoft.enroll_sdk.main_update.main_update_domain.repository

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_models.get_update_configurations.StepUpdateModel

interface MainUpdateRepository {
    suspend fun generateUpdateRequestSessionToken(request: GenerateOnboardingSessionTokenRequest): Either<SdkFailure, String>
    suspend fun getUpdateStepsConfigurations(): Either<SdkFailure, List<StepUpdateModel>>
}