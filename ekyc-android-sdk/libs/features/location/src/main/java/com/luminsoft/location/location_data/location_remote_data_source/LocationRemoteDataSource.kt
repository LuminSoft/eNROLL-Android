package com.luminsoft.location.location_data.location_remote_data_source

import com.luminsoft.core.network.BaseResponse
import com.luminsoft.location.location_data.location_models.get_token.GetCardsRequest

interface  LocationRemoteDataSource  {
    suspend fun getCards(request: GetCardsRequest): BaseResponse<Any>
}