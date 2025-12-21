package com.luminsoft.enroll_sdk.features.check_cso.check_cso_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_models.CheckCsoResponseModel
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_domain.repository.CheckCsoRepository

class CheckCsoUseCase(private val checkCsoRepository: CheckCsoRepository) :
    UseCase<Either<SdkFailure, CheckCsoResponseModel>, Nothing?> {
    override suspend fun call(params: Nothing?): Either<SdkFailure, CheckCsoResponseModel> {
        return checkCsoRepository.checkCso()
    }
}
