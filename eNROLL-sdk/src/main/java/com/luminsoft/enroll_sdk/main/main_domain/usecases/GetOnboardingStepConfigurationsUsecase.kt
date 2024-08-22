package com.luminsoft.enroll_sdk.main.main_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.StepModel
import com.luminsoft.enroll_sdk.main.main_domain.repository.MainRepository


class GetOnboardingStepConfigurationsUsecase  (private  val mainRepository: MainRepository):
    UseCase<Either<SdkFailure, List<StepModel>> ,GetOnboardingStepConfigurationsUsecaseParams> {

    override suspend fun call(params: GetOnboardingStepConfigurationsUsecaseParams): Either<SdkFailure, List<StepModel>> {
       return mainRepository.getOnBoardingStepsConfigurations()
    }
}

 class GetOnboardingStepConfigurationsUsecaseParams