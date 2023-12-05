package com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_api


import com.luminsoft.ekyc_android_sdk.core.network.ApiBaseResponse
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_models.get_token.TokenizedCardData
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_models.get_token.GetCardsRequest

import retrofit2.Response
import retrofit2.http.*


interface PasswordApi {
    @POST("/payment/GetCreditCardDetails")
    suspend fun getCards(@Body request : GetCardsRequest): Response<ApiBaseResponse<ArrayList<TokenizedCardData>>>
}