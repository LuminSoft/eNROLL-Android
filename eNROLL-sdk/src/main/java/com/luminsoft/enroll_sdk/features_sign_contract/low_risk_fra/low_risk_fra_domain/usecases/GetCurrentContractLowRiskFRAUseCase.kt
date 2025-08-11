package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_models.GetCurrentContractRequestModel
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.repository.LowRiskFRARepository
import okhttp3.ResponseBody

class GetCurrentContractLowRiskFRAUseCase(private val lowRiskFRARepository: LowRiskFRARepository) :
    UseCase<Either<SdkFailure, ResponseBody>, GetCurrentContractLowRiskFRAUseCaseParams> {

    override suspend fun call(params: GetCurrentContractLowRiskFRAUseCaseParams): Either<SdkFailure, ResponseBody> {
        val getCurrentContractRequestModel = GetCurrentContractRequestModel()
        getCurrentContractRequestModel.contractId = params.contractId
        getCurrentContractRequestModel.currentApproach = "1"
        getCurrentContractRequestModel.contractVersionNumber = params.contractVersionNumber
        getCurrentContractRequestModel.currentText = params.currentText
        getCurrentContractRequestModel.type = "5"
        return lowRiskFRARepository.getCurrentContract(getCurrentContractRequestModel)
    }
}

data class GetCurrentContractLowRiskFRAUseCaseParams(
    val contractId: String? = null,
    val contractVersionNumber: String? = null,
    val currentText: String? = null
)
