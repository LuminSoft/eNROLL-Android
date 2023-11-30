package com.luminsoft.phone_numbers.phone_numbers_data.phone_numbers_api


import com.luminsoft.core.network.ApiBaseResponse
import com.luminsoft.phone_numbers.phone_numbers_data.phone_numbers_models.get_token.TokenizedCardData
import com.luminsoft.phone_numbers.phone_numbers_data.phone_numbers_models.get_token.GetCardsRequest

import retrofit2.Response
import retrofit2.http.*

interface PhoneNumbersApi {
    @POST("/payment/GetCreditCardDetails")
    suspend fun getCards(@Body request : GetCardsRequest): Response<ApiBaseResponse<ArrayList<TokenizedCardData>>>
}