package com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_repository

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_models.CheckCsoResponseModel
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_remote_data_source.CheckCsoRemoteDataSource
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_domain.repository.CheckCsoRepository

class CheckCsoRepositoryImplementation(
    private val checkCsoRemoteDataSource: CheckCsoRemoteDataSource
) : CheckCsoRepository {
    override suspend fun checkCso(): Either<SdkFailure, CheckCsoResponseModel> {
        return when (val response = checkCsoRemoteDataSource.checkCso()) {
            is BaseResponse.Success -> Either.Right(response.data as CheckCsoResponseModel)
            is BaseResponse.Error -> Either.Left(response.error)
        }
    }
}
