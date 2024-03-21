package com.luminsoft.enroll_sdk.main.main_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.enroll_sdk.main.main_domain.repository.MainRepository


class InitializeRequestUsecase  (private  val mainRepository: MainRepository):
    UseCase<Either<SdkFailure, Null>, InitializeRequestUsecaseParams> {

    override suspend fun call(params: InitializeRequestUsecaseParams): Either<SdkFailure, Null> {

        val initializeRequestRequest = InitializeRequestRequest()
        initializeRequestRequest.deviceModel =params.deviceModel
        initializeRequestRequest.manufacturerName =params.manufacturerName
        initializeRequestRequest.imei = params.imei
        return mainRepository.initializeRequest(initializeRequestRequest)
    }
}

data class InitializeRequestUsecaseParams(
    val imei:String,
    val manufacturerName:String,
    val deviceModel:String,
)