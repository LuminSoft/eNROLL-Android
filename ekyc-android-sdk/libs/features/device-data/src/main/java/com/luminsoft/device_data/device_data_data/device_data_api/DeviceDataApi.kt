package com.luminsoft.device_data.device_data_data.device_data_api

import com.luminsoft.core.network.ApiBaseResponse
import com.luminsoft.device_data.device_data_data.device_data_models.get_token.GetCardsRequest
import com.luminsoft.device_data.device_data_data.device_data_models.get_token.TokenizedCardData
import retrofit2.Response
import retrofit2.http.*

interface DeviceDataApi {
    @POST("/payment/GetCreditCardDetails")
    suspend fun getCards(@Body request : GetCardsRequest): Response<ApiBaseResponse<ArrayList<TokenizedCardData>>>
}