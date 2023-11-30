package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_domain.repository

import arrow.core.Either
import com.luminsoft.cowpay_sdk.failures.SdkFailure
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.get_token.GetCardsRequest
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.get_token.TokenizedCardData

interface MainRepository {
     suspend fun getCards (request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>>
}