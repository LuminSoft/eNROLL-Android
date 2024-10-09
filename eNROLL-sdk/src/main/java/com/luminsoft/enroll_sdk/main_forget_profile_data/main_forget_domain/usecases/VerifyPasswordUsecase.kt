package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.generate_forget_token.VerifyPasswordRequestModel
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.repository.MainForgetRepository


class VerifyPasswordUsecase(private val mainRepository: MainForgetRepository) :
    UseCase<Either<SdkFailure, Null>, VerifyPasswordUsecaseParams> {

    override suspend fun call(params: VerifyPasswordUsecaseParams): Either<SdkFailure, Null> {

        val verifyPasswordRequestModel = VerifyPasswordRequestModel()
        verifyPasswordRequestModel.password = params.password
        verifyPasswordRequestModel.updateStepId = params.updateStepId

        return mainRepository.verifyPassword(
            verifyPasswordRequestModel
        )
    }
}

data class VerifyPasswordUsecaseParams(
    val password: String,
    val updateStepId: Int,
    )