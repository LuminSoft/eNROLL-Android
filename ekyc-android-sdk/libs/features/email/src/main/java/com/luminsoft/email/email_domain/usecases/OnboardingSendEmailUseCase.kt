package com.luminsoft.email.email_domain.usecases

import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.email.email_data.email_models.get_token.GetCardsRequest
import com.luminsoft.email.email_data.email_models.get_token.TokenizedCardData
import com.luminsoft.core.utils.UseCase
import com.luminsoft.email.email_domain.repository.EmailRepository

class GetSavedCardsUseCase  (private  val emailRepository: EmailRepository):
    UseCase<Either<SdkFailure, ArrayList<TokenizedCardData>>, GetSavedCardsUseCaseParams> {

    override suspend fun call(params: GetSavedCardsUseCaseParams): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        val getCardsRequest = GetCardsRequest()
        getCardsRequest.merchantCode =params.merchantCode
        getCardsRequest.customerReferenceId =params.customerProfileId
       return emailRepository.getCards(getCardsRequest)
    }
}

data class GetSavedCardsUseCaseParams(
    val merchantCode:String,
    val customerProfileId:String,
    )