package com.luminsoft.national_id_confirmation.national_id_confirmation_domain.usecases

import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.core.utils.UseCase
import com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.get_token.GetCardsRequest
import com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.get_token.TokenizedCardData
import com.luminsoft.national_id_confirmation.national_id_confirmation_domain.repository.NationalIdConfirmationRepository

class GetSavedCardsUseCase  (private  val nationalIdConfirmationRepository: NationalIdConfirmationRepository):
    UseCase<Either<SdkFailure, ArrayList<TokenizedCardData>>, GetSavedCardsUseCaseParams> {

    override suspend fun call(params: GetSavedCardsUseCaseParams): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        val getCardsRequest = GetCardsRequest()
        getCardsRequest.merchantCode =params.merchantCode
        getCardsRequest.customerReferenceId =params.customerProfileId
       return nationalIdConfirmationRepository.getCards(getCardsRequest)
    }
}

data class GetSavedCardsUseCaseParams(
    val merchantCode:String,
    val customerProfileId:String,
    )