package com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_api


import com.luminsoft.core.network.ApiBaseResponse
import com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.get_token.TokenizedCardData
import com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.get_token.GetCardsRequest

import retrofit2.Response
import retrofit2.http.*

interface NationalIdConfirmationApi {
    @POST("/payment/GetCreditCardDetails")
    suspend fun getCards(@Body request : GetCardsRequest): Response<ApiBaseResponse<ArrayList<TokenizedCardData>>>
}