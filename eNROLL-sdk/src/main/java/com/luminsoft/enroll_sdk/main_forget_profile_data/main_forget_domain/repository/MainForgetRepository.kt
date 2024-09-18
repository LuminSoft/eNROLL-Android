package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.generate_forget_token.GenerateForgetTokenRequest
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.generate_forget_token.VerifyPasswordRequestModel
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.get_forget_configurations.StepForgetModel

interface MainForgetRepository {
    suspend fun generateForgetRequestSessionToken(request: GenerateOnboardingSessionTokenRequest): Either<SdkFailure, String>
    suspend fun getForgetStepsConfigurations(): Either<SdkFailure, List<StepForgetModel>>
    suspend fun generateForgetRequestTokenForStep(request: GenerateForgetTokenRequest): Either<SdkFailure, String>
    suspend fun initializeForgetRequest(stepId:Int): Either<SdkFailure, Null>
    suspend fun verifyPassword(request: VerifyPasswordRequestModel): Either<SdkFailure, Null>
}