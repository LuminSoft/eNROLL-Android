package com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_remote_data_source

import ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_api.SignContractApi


class SignContractRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val singContractApi: SignContractApi
) :
    SignContractRemoteDataSource {
    override suspend fun sendSignContractOtp(): BaseResponse<Any> {
        return network.apiRequest { singContractApi.sendSignContractOtp() }
    }

    override suspend fun validateOTPSignContract(request: ValidateOTPRequestModel): BaseResponse<Any> {
        return network.apiRequest { singContractApi.validateOTPSignContract(request) }
    }
}






