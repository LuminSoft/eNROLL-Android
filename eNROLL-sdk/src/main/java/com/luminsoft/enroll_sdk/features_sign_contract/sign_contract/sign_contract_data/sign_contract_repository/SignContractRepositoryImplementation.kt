package com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_repository

import ValidateOTPRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.check_aml.check_aml_data.check_aml_models.CheckAmlResponseModel
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_models.SignContractSendOTPResponseModel
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_remote_data_source.SignContractRemoteDataSource
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_domain.repository.SignContractRepository

class SignContractRepositoryImplementation(private val signContractRemoteDataSource: SignContractRemoteDataSource) :
    SignContractRepository {
    override suspend fun sendSignContractOtp(): Either<SdkFailure, SignContractSendOTPResponseModel> {
        return when (val response = signContractRemoteDataSource.sendSignContractOtp()) {
            is BaseResponse.Success -> {
                val responseObject = response.data as SignContractSendOTPResponseModel
                Either.Right(responseObject)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun validateOTPSignContract(request: ValidateOTPRequestModel): Either<SdkFailure, Null> {
        return when (val response = signContractRemoteDataSource.validateOTPSignContract(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

}

