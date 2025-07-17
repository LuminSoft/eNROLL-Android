package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_auth_configurations.StepSignContractModel
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.repository.MainSignContractRepository


class GetSignContractStepConfigurationsUsecase(private val mainRepository: MainSignContractRepository) :
    UseCase<Either<SdkFailure, List<StepSignContractModel>>, GetSignContractStepConfigurationsUsecaseParams> {

    override suspend fun call(params: GetSignContractStepConfigurationsUsecaseParams): Either<SdkFailure, List<StepSignContractModel>> {
        return mainRepository.getSignContractStepsConfigurations()
    }
}

class GetSignContractStepConfigurationsUsecaseParams