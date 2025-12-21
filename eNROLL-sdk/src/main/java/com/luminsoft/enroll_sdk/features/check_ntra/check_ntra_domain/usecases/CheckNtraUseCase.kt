package com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_models.CheckNtraResponseModel
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_domain.repository.CheckNtraRepository

class CheckNtraUseCase(private val checkNtraRepository: CheckNtraRepository) :
    UseCase<Either<SdkFailure, CheckNtraResponseModel>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, CheckNtraResponseModel> {
        return checkNtraRepository.checkNtra()
    }
}
