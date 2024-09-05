package com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_domain.usecases

import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_domain.repository.CheckIMEIAuthRepository
import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_data.imei_models.CheckIMEIRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class AuthCheckIMEIUseCase(private val checkIMEIRepository: CheckIMEIAuthRepository) :
    UseCase<Either<SdkFailure, Null>, CheckIMEIAuthUseCaseParams> {
    override suspend fun call(params: CheckIMEIAuthUseCaseParams): Either<SdkFailure, Null> {
        val checkIMEIParameters = CheckIMEIRequestModel()
        checkIMEIParameters.imei = params.imei
        checkIMEIParameters.isFromWeb = params.isFromWeb
        return checkIMEIRepository.checkIMEIAuth(checkIMEIParameters)
    }


}

data class CheckIMEIAuthUseCaseParams(
    val imei: String,
    val isFromWeb: Boolean,
)