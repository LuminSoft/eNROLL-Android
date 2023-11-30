package com.luminsoft.password.password_data.password_remote_data_source

import com.luminsoft.core.network.BaseResponse
import com.luminsoft.password.password_data.password_models.get_token.GetCardsRequest

interface  PasswordRemoteDataSource  {
    suspend fun getCards(request: GetCardsRequest): BaseResponse<Any>
}