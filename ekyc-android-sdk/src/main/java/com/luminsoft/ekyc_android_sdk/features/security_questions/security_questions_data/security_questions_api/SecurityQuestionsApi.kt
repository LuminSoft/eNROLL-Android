package com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_data.security_questions_api


import com.luminsoft.ekyc_android_sdk.core.network.ApiBaseResponse
import com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_data.security_questions_models.get_token.TokenizedCardData
import com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_data.security_questions_models.get_token.GetCardsRequest

import retrofit2.Response
import retrofit2.http.*

interface SecurityQuestionsApi {
    @POST("/payment/GetCreditCardDetails")
    suspend fun getCards(@Body request : GetCardsRequest): Response<com.luminsoft.ekyc_android_sdk.core.network.ApiBaseResponse<ArrayList<TokenizedCardData>>>
}