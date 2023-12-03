package com.luminsoft.device_data.device_data_domain.usecases

import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.device_data.device_data_data.device_data_models.get_token.GetCardsRequest
import com.luminsoft.device_data.device_data_data.device_data_models.get_token.TokenizedCardData
import com.luminsoft.core.utils.UseCase
import com.luminsoft.device_data.device_data_domain.repository.DeviceDataRepository

class GetSavedCardsUseCase  (private  val deviceDataRepository: DeviceDataRepository):
    UseCase<Either<SdkFailure, ArrayList<TokenizedCardData>>, GetSavedCardsUseCaseParams> {

    override suspend fun call(params: GetSavedCardsUseCaseParams): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        val getCardsRequest = GetCardsRequest()
        getCardsRequest.merchantCode =params.merchantCode
        getCardsRequest.customerReferenceId =params.customerProfileId
       return deviceDataRepository.getCards(getCardsRequest)
    }
}

data class GetSavedCardsUseCaseParams(
    val merchantCode:String,
    val customerProfileId:String,
    )