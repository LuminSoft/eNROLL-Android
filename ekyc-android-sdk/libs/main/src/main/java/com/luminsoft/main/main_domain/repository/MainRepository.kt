package com.luminsoft.main.main_domain.repository

import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.main.main_data.main_models.get_token.GetCardsRequest
import com.luminsoft.main.main_data.main_models.get_token.TokenizedCardData

interface MainRepository {
     suspend fun getCards (request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>>
}