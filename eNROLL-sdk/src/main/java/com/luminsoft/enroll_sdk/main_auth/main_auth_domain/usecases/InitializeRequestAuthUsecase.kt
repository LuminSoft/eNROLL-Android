package com.luminsoft.enroll_sdk.main_auth.main_auth_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.enroll_sdk.main_auth.main_auth_domain.repository.MainAuthRepository


class InitializeRequestAuthUsecase(private val mainRepository: MainAuthRepository) :
    UseCase<Either<SdkFailure, Null>, InitializeRequestAuthUsecaseParams> {

    override suspend fun call(params: InitializeRequestAuthUsecaseParams): Either<SdkFailure, Null> {

        val initializeRequestRequest = InitializeRequestRequest()
        initializeRequestRequest.deviceModel = params.deviceModel
        initializeRequestRequest.manufacturerName = params.manufacturerName
        initializeRequestRequest.imei = params.imei
        return mainRepository.initializeAuthRequest(initializeRequestRequest)
    }
}

data class InitializeRequestAuthUsecaseParams(
    val imei: String,
    val manufacturerName: String,
    val deviceModel: String,
)