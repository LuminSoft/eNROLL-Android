package com.luminsoft.ekyc_android_sdk.features.location.location_data.location_repository


import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.network.ApiBaseResponse
import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_models.get_token.TokenizedCardData

import com.luminsoft.ekyc_android_sdk.features.location.location_domain.repository.LocationRepository
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_remote_data_source.LocationRemoteDataSource

class LocationRepositoryImplementation(private val locationRemoteDataSource: LocationRemoteDataSource):
    LocationRepository {
    override suspend fun getCards(request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        return when (val response = locationRemoteDataSource.getCards(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<ArrayList<TokenizedCardData>>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

