package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_remote_data_source

import com.luminsoft.cowpay_sdk.network.BaseResponse
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.get_token.GetCardsRequest

interface  MainRemoteDataSource  {
    suspend fun getCards(request: GetCardsRequest):BaseResponse<Any>
}