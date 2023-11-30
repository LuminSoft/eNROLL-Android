package com.luminsoft.location.location_data.location_repository


import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.core.network.ApiBaseResponse
import com.luminsoft.core.network.BaseResponse
import com.luminsoft.location.location_data.location_models.get_token.GetCardsRequest
import com.luminsoft.location.location_data.location_models.get_token.TokenizedCardData

import com.luminsoft.location.location_domain.repository.LocationRepository
import com.luminsoft.location.location_data.location_remote_data_source.LocationRemoteDataSource

class LocationRepositoryImplementation(private val cardsPaymentRemoteDataSource: LocationRemoteDataSource):
    LocationRepository {

    override suspend fun getCards(request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        return when (val response = cardsPaymentRemoteDataSource.getCards(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<ArrayList<TokenizedCardData>>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

