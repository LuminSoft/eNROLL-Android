package com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_domain.usecases

import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.utils.UseCase
import com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_data.security_questions_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_data.security_questions_models.get_token.TokenizedCardData
import com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_domain.repository.SecurityQuestionsRepository

class GetSavedCardsUseCase  (private  val securityQuestionsRepository: SecurityQuestionsRepository):
    UseCase<Either<SdkFailure, ArrayList<TokenizedCardData>>, GetSavedCardsUseCaseParams> {

    override suspend fun call(params: GetSavedCardsUseCaseParams): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        val getCardsRequest = GetCardsRequest()
        getCardsRequest.merchantCode =params.merchantCode
        getCardsRequest.customerReferenceId =params.customerProfileId
       return securityQuestionsRepository.getCards(getCardsRequest)
    }
}

data class GetSavedCardsUseCaseParams(
    val merchantCode:String,
    val customerProfileId:String,
    )