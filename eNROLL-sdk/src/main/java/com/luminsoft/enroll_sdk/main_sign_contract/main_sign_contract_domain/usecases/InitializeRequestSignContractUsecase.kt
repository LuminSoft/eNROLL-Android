package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.repository.MainSignContractRepository


class InitializeRequestSignContractUsecase(private val mainRepository: MainSignContractRepository) :
    UseCase<Either<SdkFailure, Null>, InitializeRequestSignContractUsecaseParams> {

    override suspend fun call(params: InitializeRequestSignContractUsecaseParams): Either<SdkFailure, Null> {

        val initializeRequestRequest = InitializeRequestRequest()
        initializeRequestRequest.deviceModel = params.deviceModel
        initializeRequestRequest.manufacturerName = params.manufacturerName
        initializeRequestRequest.imei = params.imei
        return mainRepository.initializeSignContractRequest(initializeRequestRequest)
    }
}

data class InitializeRequestSignContractUsecaseParams(
    val imei: String,
    val manufacturerName: String,
    val deviceModel: String,
)