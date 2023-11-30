package com.luminsoft.password.password_domain.repository

import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.password.password_data.password_models.get_token.GetCardsRequest
import com.luminsoft.password.password_data.password_models.get_token.TokenizedCardData

interface PasswordRepository {
     suspend fun getCards (request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>>
}