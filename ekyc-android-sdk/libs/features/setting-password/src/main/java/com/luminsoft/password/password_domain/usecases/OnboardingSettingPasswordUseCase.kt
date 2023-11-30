package com.luminsoft.password.password_domain.usecases

import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.core.utils.UseCase
import com.luminsoft.password.password_data.password_models.get_token.GetCardsRequest
import com.luminsoft.password.password_data.password_models.get_token.TokenizedCardData
import com.luminsoft.password.password_domain.repository.PasswordRepository

class GetSavedCardsUseCase  (private  val passwordRepository: PasswordRepository):
    UseCase<Either<SdkFailure, ArrayList<TokenizedCardData>>, GetSavedCardsUseCaseParams> {

    override suspend fun call(params: GetSavedCardsUseCaseParams): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        val getCardsRequest = GetCardsRequest()
        getCardsRequest.merchantCode =params.merchantCode
        getCardsRequest.customerReferenceId =params.customerProfileId
       return passwordRepository.getCards(getCardsRequest)
    }
}

data class GetSavedCardsUseCaseParams(
    val merchantCode:String,
    val customerProfileId:String,
    )