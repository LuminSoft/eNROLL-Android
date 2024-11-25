package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.generate_forget_token.GenerateForgetTokenRequest
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.repository.MainForgetRepository


class GenerateForgetSessionForStepTokenUsecase(private val mainRepository: MainForgetRepository) :
    UseCase<Either<SdkFailure, String>, GenerateForgetSessionTokenForStepUsecaseParams> {

    override suspend fun call(params: GenerateForgetSessionTokenForStepUsecaseParams): Either<SdkFailure, String> {

        val generateOnboardingSessionTokenRequest = GenerateForgetTokenRequest()
        generateOnboardingSessionTokenRequest.step = params.step
        generateOnboardingSessionTokenRequest.nationalIdOrPassportNumber =
            params.nationalIdOrPassportNumber
        return mainRepository.generateForgetRequestTokenForStep(
            generateOnboardingSessionTokenRequest
        )
    }
}

data class GenerateForgetSessionTokenForStepUsecaseParams(
    val step: Int,
    val nationalIdOrPassportNumber: String,
)