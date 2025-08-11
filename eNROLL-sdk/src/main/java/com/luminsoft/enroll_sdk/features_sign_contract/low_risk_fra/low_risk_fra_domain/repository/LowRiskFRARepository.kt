package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.repository

import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_models.GetCurrentContractRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_models.ValidateOTPLowRiskFRARequestModel
import okhttp3.ResponseBody

interface LowRiskFRARepository {
    suspend fun sendLowRiskFRAOtp(): Either<SdkFailure, Null>
    suspend fun validateOTPLowRiskFRA(request: ValidateOTPLowRiskFRARequestModel): Either<SdkFailure, Null>
    suspend fun getCurrentContract(request: GetCurrentContractRequestModel): Either<SdkFailure, ResponseBody>
    suspend fun getSignContractFile(): Either<SdkFailure, ResponseBody>
}