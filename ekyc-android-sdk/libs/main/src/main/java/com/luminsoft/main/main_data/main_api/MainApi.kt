package com.luminsoft.main.main_data.main_api


import com.luminsoft.core.network.ApiBaseResponse
import com.luminsoft.main.main_data.main_models.get_token.TokenizedCardData
import com.luminsoft.main.main_data.main_models.get_token.GetCardsRequest

import retrofit2.Response
import retrofit2.http.*

interface MainApi {
    @POST("/payment/GetCreditCardDetails")
    suspend fun getCards(@Body request : GetCardsRequest): Response<ApiBaseResponse<ArrayList<TokenizedCardData>>>
}