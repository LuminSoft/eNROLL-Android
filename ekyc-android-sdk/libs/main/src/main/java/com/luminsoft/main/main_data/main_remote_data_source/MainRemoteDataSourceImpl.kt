package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_remote_data_source
import com.luminsoft.cowpay_sdk.network.BaseRemoteDataSource
import com.luminsoft.cowpay_sdk.network.BaseResponse
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.get_token.GetCardsRequest
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_api.CardsPaymentApi


class  MainRemoteDataSourceImpl (private val network: BaseRemoteDataSource, private val cardsPaymentApi: CardsPaymentApi):
    CardsPaymentRemoteDataSource {

    override suspend fun getCards(request: GetCardsRequest, ): BaseResponse<Any> {

            return network.apiRequest { cardsPaymentApi.getCards(request) }

    }
}






