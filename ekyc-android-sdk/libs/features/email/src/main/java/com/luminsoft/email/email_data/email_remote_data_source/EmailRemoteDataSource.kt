package com.luminsoft.email.email_data.email_remote_data_source

import com.luminsoft.core.network.BaseResponse
import com.luminsoft.email.email_data.email_models.get_token.GetCardsRequest

interface  EmailRemoteDataSource  {
    suspend fun getCards(request: GetCardsRequest): BaseResponse<Any>
}