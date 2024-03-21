package com.luminsoft.enroll_sdk.features.location.location_data.location_repository


import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.PostLocationRequestModel

import com.luminsoft.enroll_sdk.features.location.location_domain.repository.LocationRepository
import com.luminsoft.enroll_sdk.features.location.location_data.location_remote_data_source.LocationRemoteDataSource

class LocationRepositoryImplementation(private val locationRemoteDataSource: LocationRemoteDataSource) :
    LocationRepository {
    override suspend fun postLocation(request: PostLocationRequestModel): Either<SdkFailure, Null> {
        return when (val response = locationRemoteDataSource.postLocation(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

