package com.luminsoft.device_data.device_data_data.device_data_remote_data_source
import com.luminsoft.core.network.BaseRemoteDataSource
import com.luminsoft.core.network.BaseResponse
import com.luminsoft.device_data.device_data_data.device_data_models.get_token.GetCardsRequest
import com.luminsoft.device_data.device_data_data.device_data_api.DeviceDataApi

class  DeviceDataRemoteDataSourceImpl (private val network: BaseRemoteDataSource, private val cardsPaymentApi: DeviceDataApi):
    DeviceDataRemoteDataSource {

    override suspend fun getCards(request: GetCardsRequest, ): BaseResponse<Any> {

            return network.apiRequest { cardsPaymentApi.getCards(request) }

    }
}






