package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.repository.LowRiskFRARepository
import okhttp3.ResponseBody

class GetSignContractFileLowRiskFRAUseCase(private val lowRiskFRARepository: LowRiskFRARepository) :
    UseCase<Either<SdkFailure, ResponseBody>, GetSignContractFileLowRiskFRAUseCaseParams> {

    override suspend fun call(params: GetSignContractFileLowRiskFRAUseCaseParams): Either<SdkFailure, ResponseBody> {
        return lowRiskFRARepository.getSignContractFile()
    }
}

class GetSignContractFileLowRiskFRAUseCaseParams