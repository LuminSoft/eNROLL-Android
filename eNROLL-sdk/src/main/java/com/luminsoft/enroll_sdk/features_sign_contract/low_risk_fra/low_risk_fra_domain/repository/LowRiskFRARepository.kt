package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.repository

import GetCurrentContractRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface LowRiskFRARepository {
    suspend fun sendLowRiskFRAOtp(): Either<SdkFailure, Null>
    suspend fun validateOTPLowRiskFRA(request: GetCurrentContractRequestModel): Either<SdkFailure, Null>
}