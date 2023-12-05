package com.luminsoft.ekyc_android_sdk.features.device_data.device_data_data.device_data_remote_data_source
import com.luminsoft.ekyc_android_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_data.device_data_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_data.device_data_api.DeviceDataApi

class  DeviceDataRemoteDataSourceImpl (private val network:BaseRemoteDataSource, private val cardsPaymentApi: DeviceDataApi):
    DeviceDataRemoteDataSource {

    override suspend fun getCards(request: GetCardsRequest, ): BaseResponse<Any> {

            return network.apiRequest { cardsPaymentApi.getCards(request) }

    }
}






