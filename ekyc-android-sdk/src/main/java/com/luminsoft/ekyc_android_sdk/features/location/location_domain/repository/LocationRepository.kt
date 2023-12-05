package com.luminsoft.ekyc_android_sdk.features.location.location_domain.repository

import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_models.get_token.TokenizedCardData

interface LocationRepository {
     suspend fun getCards (request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>>
}