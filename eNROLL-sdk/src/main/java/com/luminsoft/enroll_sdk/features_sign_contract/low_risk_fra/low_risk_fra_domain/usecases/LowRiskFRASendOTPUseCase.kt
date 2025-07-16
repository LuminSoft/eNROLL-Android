package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.repository.LowRiskFRARepository

class LowRiskFRASendOTPUseCase(private val phoneRepository: LowRiskFRARepository) :
    UseCase<Either<SdkFailure, Null>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, Null> {
        return phoneRepository.sendLowRiskFRAOtp()
    }
}

