package com.luminsoft.main.main_domain.usecases

import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.main.main_data.main_models.get_token.GetCardsRequest
import com.luminsoft.main.main_data.main_models.get_token.TokenizedCardData
import com.luminsoft.core.utils.UseCase
import com.luminsoft.main.main_domain.repository.MainRepository

class GetSavedCardsUseCase  (private  val mainRepository: MainRepository):
    UseCase<Either<SdkFailure, ArrayList<TokenizedCardData>>, GetSavedCardsUseCaseParams> {

    override suspend fun call(params: GetSavedCardsUseCaseParams): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        val getCardsRequest = GetCardsRequest()
        getCardsRequest.merchantCode =params.merchantCode
        getCardsRequest.customerReferenceId =params.customerProfileId
       return mainRepository.getCards(getCardsRequest)
    }
}

data class GetSavedCardsUseCaseParams(
    val merchantCode:String,
    val customerProfileId:String,
    )