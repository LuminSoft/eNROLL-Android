package com.luminsoft.ekyc_android_sdk.features.location.location_data.location_api


import com.luminsoft.ekyc_android_sdk.core.network.ApiBaseResponse
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_models.get_token.TokenizedCardData
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_models.get_token.GetCardsRequest

import retrofit2.Response
import retrofit2.http.*

interface LocationApi {
    @POST("/payment/GetCreditCardDetails")
    suspend fun getCards(@Body request : GetCardsRequest): Response<ApiBaseResponse<ArrayList<TokenizedCardData>>>
}