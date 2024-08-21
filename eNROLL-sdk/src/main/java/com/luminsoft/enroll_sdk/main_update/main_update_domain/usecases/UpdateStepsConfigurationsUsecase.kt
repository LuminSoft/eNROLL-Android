package com.luminsoft.enroll_sdk.main_update.main_update_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_models.get_update_configurations.StepUpdateModel
import com.luminsoft.enroll_sdk.main_update.main_update_domain.repository.MainUpdateRepository


class UpdateStepsConfigurationsUsecase(private val mainRepository: MainUpdateRepository) :
    UseCase<Either<SdkFailure, List<StepUpdateModel>>, GetUpdateStepConfigurationsUsecaseParams> {

    override suspend fun call(params: GetUpdateStepConfigurationsUsecaseParams): Either<SdkFailure, List<StepUpdateModel>> {
        return mainRepository.getUpdateStepsConfigurations()
    }
}

class GetUpdateStepConfigurationsUsecaseParams