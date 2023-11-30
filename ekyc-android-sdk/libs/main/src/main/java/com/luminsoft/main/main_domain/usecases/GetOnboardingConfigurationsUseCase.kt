package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_domain.usecases

import arrow.core.Either
import com.luminsoft.cowpay_sdk.failures.SdkFailure
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.get_token.GetCardsRequest
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.get_token.TokenizedCardData
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_domain.repository.CardsPaymentRepository
import com.luminsoft.core.utils.UseCase

class GetSavedCardsUseCase  (private  val cardsPaymentRepository: CardsPaymentRepository):
    UseCase<Either<SdkFailure, ArrayList<TokenizedCardData>>, GetSavedCardsUseCaseParams> {

    override suspend fun call(params: GetSavedCardsUseCaseParams): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        val getCardsRequest = GetCardsRequest()
        getCardsRequest.merchantCode =params.merchantCode
        getCardsRequest.customerReferenceId =params.customerProfileId
       return cardsPaymentRepository.getCards(getCardsRequest)
    }
}

data class GetSavedCardsUseCaseParams(
    val merchantCode:String,
    val customerProfileId:String,
    )