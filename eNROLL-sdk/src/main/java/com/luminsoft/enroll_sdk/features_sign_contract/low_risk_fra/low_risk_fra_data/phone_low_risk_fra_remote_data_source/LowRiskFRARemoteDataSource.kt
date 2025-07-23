package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.phone_low_risk_fra_remote_data_source

import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_models.GetCurrentContractRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_models.ValidateOTPLowRiskFRARequestModel

interface LowRiskFRARemoteDataSource {
    suspend fun sendLowRiskFRAOtp(): BaseResponse<Any>
    suspend fun validateOTPLowRiskFRA(request: ValidateOTPLowRiskFRARequestModel): BaseResponse<Any>
    suspend fun getCurrentContract(request: GetCurrentContractRequestModel): BaseResponse<Any>
    suspend fun getSignContractFile(): BaseResponse<Any>

}