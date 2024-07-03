package com.luminsoft.enroll_sdk.main_auth.main_auth_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_models.get_auth_configurations.StepAuthModel
import com.luminsoft.enroll_sdk.main_auth.main_auth_domain.repository.MainAuthRepository


class GetAuthStepConfigurationsUsecase(private val mainRepository: MainAuthRepository) :
    UseCase<Either<SdkFailure, List<StepAuthModel>>, GetAuthStepConfigurationsUsecaseParams> {

    override suspend fun call(params: GetAuthStepConfigurationsUsecaseParams): Either<SdkFailure, List<StepAuthModel>> {
        return mainRepository.getAuthStepsConfigurations()
    }
}

class GetAuthStepConfigurationsUsecaseParams {}