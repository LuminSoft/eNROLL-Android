package com.luminsoft.enroll_sdk.main_update.main_update_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.main_update.main_update_domain.repository.MainUpdateRepository


class UpdateStepsInitRequestUsecase(private val mainRepository: MainUpdateRepository) :
    UseCase<Either<SdkFailure, Null>, UpdateStepIdParam> {

    override suspend fun call(params: UpdateStepIdParam): Either<SdkFailure, Null> {
        return mainRepository.updateStepsInitRequest(updateStepId = params.updateStepId)
    }
}

data class UpdateStepIdParam(
    val updateStepId: Int
)
