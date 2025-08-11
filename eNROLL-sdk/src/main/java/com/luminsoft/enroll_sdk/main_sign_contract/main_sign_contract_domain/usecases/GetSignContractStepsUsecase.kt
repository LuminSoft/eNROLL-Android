package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_sign_contract_steps.StepSignContractModel
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.repository.MainSignContractRepository


class GetSignContractStepsUsecase(private val mainRepository: MainSignContractRepository) :
    UseCase<Either<SdkFailure, StepSignContractModel>, GetSignContractStepsUsecaseParams> {

    override suspend fun call(params: GetSignContractStepsUsecaseParams): Either<SdkFailure, StepSignContractModel> {

        return mainRepository.getSignContractSteps()
    }
}

class GetSignContractStepsUsecaseParams