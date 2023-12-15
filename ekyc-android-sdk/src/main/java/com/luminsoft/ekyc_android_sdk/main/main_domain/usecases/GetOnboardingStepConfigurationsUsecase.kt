package com.luminsoft.ekyc_android_sdk.main.main_domain.usecases

import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.utils.UseCase
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.get_onboaring_configurations.StepModel
import com.luminsoft.ekyc_android_sdk.main.main_domain.repository.MainRepository


class GetOnboardingStepConfigurationsUsecase  (private  val mainRepository: MainRepository):
    UseCase<Either<SdkFailure, List<StepModel>> ,GetOnboardingStepConfigurationsUsecaseParams> {

    override suspend fun call(params: GetOnboardingStepConfigurationsUsecaseParams): Either<SdkFailure, List<StepModel>> {
       return mainRepository.getOnBoardingStepsConfigurations()
    }
}

 class GetOnboardingStepConfigurationsUsecaseParams{}