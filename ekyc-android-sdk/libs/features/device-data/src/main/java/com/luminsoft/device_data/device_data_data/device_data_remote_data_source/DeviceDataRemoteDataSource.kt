package com.luminsoft.device_data.device_data_data.device_data_remote_data_source

import com.luminsoft.core.network.BaseResponse
import com.luminsoft.device_data.device_data_data.device_data_models.get_token.GetCardsRequest

interface  DeviceDataRemoteDataSource  {
    suspend fun getCards(request: GetCardsRequest): BaseResponse<Any>
}