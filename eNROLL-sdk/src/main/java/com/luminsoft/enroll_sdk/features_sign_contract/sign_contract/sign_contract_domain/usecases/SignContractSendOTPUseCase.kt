package com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_models.SignContractSendOTPResponseModel
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_domain.repository.SignContractRepository

class SignContractSendOTPUseCase(private val signContractRepository: SignContractRepository) :
    UseCase<Either<SdkFailure, SignContractSendOTPResponseModel>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, SignContractSendOTPResponseModel> {
        return signContractRepository.sendSignContractOtp()
    }
}


