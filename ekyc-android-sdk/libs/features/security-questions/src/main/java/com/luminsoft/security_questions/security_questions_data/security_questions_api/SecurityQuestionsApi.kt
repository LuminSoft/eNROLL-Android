package com.luminsoft.security_questions.security_questions_data.security_questions_api


import com.luminsoft.core.network.ApiBaseResponse
import com.luminsoft.security_questions.security_questions_data.security_questions_models.get_token.TokenizedCardData
import com.luminsoft.security_questions.security_questions_data.security_questions_models.get_token.GetCardsRequest

import retrofit2.Response
import retrofit2.http.*

interface SecurityQuestionsApi {
    @POST("/payment/GetCreditCardDetails")
    suspend fun getCards(@Body request : GetCardsRequest): Response<ApiBaseResponse<ArrayList<TokenizedCardData>>>
}