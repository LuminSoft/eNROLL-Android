package com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_domain.usecases

import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models.get_token.TokenizedCardData
import com.luminsoft.ekyc_android_sdk.core.utils.UseCase
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_domain.repository.FaceCaptureRepository

class GetSavedCardsUseCase  (private  val faceCaptureRepository: FaceCaptureRepository):
    UseCase<Either<SdkFailure, ArrayList<TokenizedCardData>>, GetSavedCardsUseCaseParams> {

    override suspend fun call(params: GetSavedCardsUseCaseParams): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        val getCardsRequest = GetCardsRequest()
        getCardsRequest.merchantCode =params.merchantCode
        getCardsRequest.customerReferenceId =params.customerProfileId
       return faceCaptureRepository.getCards(getCardsRequest)
    }
}

data class GetSavedCardsUseCaseParams(
    val merchantCode:String,
    val customerProfileId:String,
    )