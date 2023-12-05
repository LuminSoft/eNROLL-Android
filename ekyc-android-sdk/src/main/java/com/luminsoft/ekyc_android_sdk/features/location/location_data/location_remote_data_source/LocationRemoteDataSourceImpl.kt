package com.luminsoft.ekyc_android_sdk.features.location.location_data.location_remote_data_source
import com.luminsoft.ekyc_android_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_api.LocationApi


class  LocationRemoteDataSourceImpl (private val network: BaseRemoteDataSource, private val cardsPaymentApi: LocationApi):
    LocationRemoteDataSource {

    override suspend fun getCards(request: GetCardsRequest, ): BaseResponse<Any> {

            return network.apiRequest { cardsPaymentApi.getCards(request) }

    }
}






