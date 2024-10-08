package com.luminsoft.enroll_sdk.main_update.main_update_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_models.get_update_configurations.StepUpdateModel
import com.luminsoft.enroll_sdk.main_update.main_update_data.models.UpdateVerificationMethodResponse

interface MainUpdateRepository {
    suspend fun generateUpdateRequestSessionToken(request: GenerateOnboardingSessionTokenRequest): Either<SdkFailure, String>
    suspend fun getUpdateStepsConfigurations(): Either<SdkFailure, List<StepUpdateModel>>
    suspend fun updateStepsInitRequest(updateStepId: Int): Either<SdkFailure, Null>
    suspend fun getUpdateAuthenticationMethod(updateStepId: Int): Either<SdkFailure, UpdateVerificationMethodResponse>
}