package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.phone_low_risk_fra_repository

import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_models.GetCurrentContractRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_models.ValidateOTPLowRiskFRARequestModel
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.phone_low_risk_fra_remote_data_source.LowRiskFRARemoteDataSource
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.repository.LowRiskFRARepository
import okhttp3.ResponseBody

class LowRiskFRARepositoryImplementation(private val mailRemoteDataSource: LowRiskFRARemoteDataSource) :
    LowRiskFRARepository {
    override suspend fun sendLowRiskFRAOtp(): Either<SdkFailure, Null> {
        return when (val response = mailRemoteDataSource.sendLowRiskFRAOtp()) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun validateOTPLowRiskFRA(request: ValidateOTPLowRiskFRARequestModel): Either<SdkFailure, Null> {
        return when (val response = mailRemoteDataSource.validateOTPLowRiskFRA(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun getCurrentContract(request: GetCurrentContractRequestModel): Either<SdkFailure, ResponseBody> {
        return when (val response = mailRemoteDataSource.getCurrentContract(request)) {
            is BaseResponse.Success -> {
                val responseBody = response.data as ResponseBody
                Either.Right(responseBody)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

}

