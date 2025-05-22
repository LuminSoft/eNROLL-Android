package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.repository.MainForgetRepository


class InitializeForgetRequestUsecase(private val mainRepository: MainForgetRepository) :
    UseCase<Either<SdkFailure, Null>, Int> {

    override suspend fun call(params: Int): Either<SdkFailure, Null> {
        return mainRepository.initializeForgetRequest(params)
    }
}
