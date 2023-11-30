package com.luminsoft.email.email_data.email_api


import com.luminsoft.core.network.ApiBaseResponse
import com.luminsoft.email.email_data.email_models.get_token.GetCardsRequest
import com.luminsoft.email.email_data.email_models.get_token.TokenizedCardData
import retrofit2.Response
import retrofit2.http.*

interface EmailApi {
    @POST("/payment/GetCreditCardDetails")
    suspend fun getCards(@Body request : GetCardsRequest): Response<ApiBaseResponse<ArrayList<TokenizedCardData>>>
}