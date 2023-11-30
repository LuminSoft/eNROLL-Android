package com.luminsoft.email.email_domain.repository

import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.email.email_data.email_models.get_token.GetCardsRequest
import com.luminsoft.email.email_data.email_models.get_token.TokenizedCardData

interface EmailRepository {
     suspend fun getCards (request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>>
}