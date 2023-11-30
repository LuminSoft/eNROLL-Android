package com.luminsoft.phone_numbers.phone_numbers_domain.usecases

import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.core.utils.UseCase
import com.luminsoft.phone_numbers.phone_numbers_data.phone_numbers_models.get_token.GetCardsRequest
import com.luminsoft.phone_numbers.phone_numbers_data.phone_numbers_models.get_token.TokenizedCardData
import com.luminsoft.phone_numbers.phone_numbers_domain.repository.PhoneNumbersRepository

class GetSavedCardsUseCase  (private  val phoneNumbersRepository: PhoneNumbersRepository):
    UseCase<Either<SdkFailure, ArrayList<TokenizedCardData>>, GetSavedCardsUseCaseParams> {

    override suspend fun call(params: GetSavedCardsUseCaseParams): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        val getCardsRequest = GetCardsRequest()
        getCardsRequest.merchantCode =params.merchantCode
        getCardsRequest.customerReferenceId =params.customerProfileId
       return phoneNumbersRepository.getCards(getCardsRequest)
    }
}

data class GetSavedCardsUseCaseParams(
    val merchantCode:String,
    val customerProfileId:String,
    )