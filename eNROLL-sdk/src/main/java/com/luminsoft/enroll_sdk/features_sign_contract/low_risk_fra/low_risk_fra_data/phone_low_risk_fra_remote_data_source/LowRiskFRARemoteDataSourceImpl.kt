package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.phone_low_risk_fra_remote_data_source

import GetCurrentContractRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_api.LowRiskFRAApi


class LowRiskFRARemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val lowRiskFRAApi: LowRiskFRAApi
) :
    LowRiskFRARemoteDataSource {
    override suspend fun sendLowRiskFRAOtp(): BaseResponse<Any> {
        return network.apiRequest { lowRiskFRAApi.sendLowRiskFRAOtp() }
    }

    override suspend fun validateOTPLowRiskFRA(request: GetCurrentContractRequestModel): BaseResponse<Any> {
        return network.apiRequest { lowRiskFRAApi.validateOTPLowRiskFRA(request) }
    }
}






