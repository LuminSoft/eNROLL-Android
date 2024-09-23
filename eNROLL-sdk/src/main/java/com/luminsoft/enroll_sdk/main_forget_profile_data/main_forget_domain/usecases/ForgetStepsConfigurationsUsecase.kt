package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.get_forget_configurations.StepForgetModel
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.repository.MainForgetRepository


class ForgetStepsConfigurationsUsecase(private val mainRepository: MainForgetRepository) :
    UseCase<Either<SdkFailure, List<StepForgetModel>>, GetForgetStepConfigurationsUsecaseParams> {

    override suspend fun call(params: GetForgetStepConfigurationsUsecaseParams): Either<SdkFailure, List<StepForgetModel>> {
        return mainRepository.getForgetStepsConfigurations()
    }
}

class GetForgetStepConfigurationsUsecaseParams