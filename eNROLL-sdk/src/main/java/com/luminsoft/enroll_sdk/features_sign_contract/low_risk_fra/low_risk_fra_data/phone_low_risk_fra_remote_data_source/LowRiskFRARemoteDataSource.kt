package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.phone_low_risk_fra_remote_data_source

import ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface LowRiskFRARemoteDataSource {
    suspend fun sendLowRiskFRAOtp(): BaseResponse<Any>
    suspend fun validateOTPLowRiskFRA(request: ValidateOTPRequestModel): BaseResponse<Any>

}