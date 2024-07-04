package com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_data.location_auth_repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.PostLocationRequestModel
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_data.location_auth_remote_data_source.LocationAuthRemoteDataSource
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_domain.repository.LocationAuthRepository

class LocationAuthRepositoryImplementation(private val locationRemoteDataSource: LocationAuthRemoteDataSource) :
    LocationAuthRepository {
    override suspend fun postAuthLocation(request: PostLocationRequestModel): Either<SdkFailure, Null> {
        return when (val response = locationRemoteDataSource.postAuthLocation(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }
}

