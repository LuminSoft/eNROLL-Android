package com.luminsoft.location.location_domain.repository

import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.location.location_data.location_models.get_token.GetCardsRequest
import com.luminsoft.location.location_data.location_models.get_token.TokenizedCardData

interface LocationRepository {
     suspend fun getCards (request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>>
}