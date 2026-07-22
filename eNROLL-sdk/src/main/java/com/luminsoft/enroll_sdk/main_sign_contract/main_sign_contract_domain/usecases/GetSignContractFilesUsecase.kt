package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_sign_contract_files.SignContractFileItemModel
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.repository.MainSignContractRepository


class GetSignContractFilesUsecase(private val mainRepository: MainSignContractRepository) :
    UseCase<Either<SdkFailure, List<SignContractFileItemModel>>, GetSignContractFilesUsecaseParams> {

    override suspend fun call(params: GetSignContractFilesUsecaseParams): Either<SdkFailure, List<SignContractFileItemModel>> {
        return mainRepository.getSignContractFiles()
    }
}

class GetSignContractFilesUsecaseParams
