package com.luminsoft.ekyc_android_sdk.main.main_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.utils.UseCase
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.ekyc_android_sdk.main.main_domain.repository.MainRepository


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