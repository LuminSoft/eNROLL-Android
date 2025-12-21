package com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_repository

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_models.CheckNtraResponseModel
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_remote_data_source.CheckNtraRemoteDataSource
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_domain.repository.CheckNtraRepository

class CheckNtraRepositoryImplementation(private val checkNtraRemoteDataSource: CheckNtraRemoteDataSource) :
    CheckNtraRepository {
    override suspend fun checkNtra(): Either<SdkFailure, CheckNtraResponseModel> {
        return when (val response = checkNtraRemoteDataSource.checkNtra()) {
            is BaseResponse.Success -> {
                val responseObject = response.data as CheckNtraResponseModel
                Either.Right(responseObject)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

}
