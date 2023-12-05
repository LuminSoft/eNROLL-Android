package com.luminsoft.ekyc_android_sdk.features.device_data.device_data_data.device_data_remote_data_source

import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_data.device_data_models.get_token.GetCardsRequest

interface  DeviceDataRemoteDataSource  {
    suspend fun getCards(request: GetCardsRequest): BaseResponse<Any>
}