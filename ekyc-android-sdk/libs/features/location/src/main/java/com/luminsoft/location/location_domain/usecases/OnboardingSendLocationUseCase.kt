package com.luminsoft.location.location_domain.usecases

import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.core.utils.UseCase
import com.luminsoft.location.location_data.location_models.get_token.GetCardsRequest
import com.luminsoft.location.location_data.location_models.get_token.TokenizedCardData
import com.luminsoft.location.location_domain.repository.LocationRepository

class GetSavedCardsUseCase  (private  val cardsPaymentRepository: LocationRepository):
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