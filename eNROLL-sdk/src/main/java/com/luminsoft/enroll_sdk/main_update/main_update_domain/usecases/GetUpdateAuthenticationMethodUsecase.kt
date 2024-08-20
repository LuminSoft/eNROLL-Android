package com.luminsoft.enroll_sdk.main_update.main_update_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main_update.main_update_data.models.UpdateVerificationMethodResponse
import com.luminsoft.enroll_sdk.main_update.main_update_domain.repository.MainUpdateRepository


class GetUpdateAuthenticationMethodUsecase(private val mainRepository: MainUpdateRepository) :
    UseCase<Either<SdkFailure, UpdateVerificationMethodResponse>, Int> {

    override suspend fun call(params: Int): Either<SdkFailure, UpdateVerificationMethodResponse> {
        return mainRepository.getUpdateAuthenticationMethod(params)
    }
}

