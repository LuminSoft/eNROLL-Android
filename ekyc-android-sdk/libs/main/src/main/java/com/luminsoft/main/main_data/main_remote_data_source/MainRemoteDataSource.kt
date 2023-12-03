package com.luminsoft.main.main_data.main_remote_data_source

import com.luminsoft.core.network.BaseResponse
import com.luminsoft.main.main_data.main_models.get_token.GetCardsRequest

interface  MainRemoteDataSource  {
    suspend fun getCards(request: GetCardsRequest): BaseResponse<Any>
}