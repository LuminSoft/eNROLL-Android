package com.luminsoft.enroll_sdk.features.email.email_domain.repository

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.get_token.GetCardsRequest
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.get_token.TokenizedCardData

interface EmailRepository {
     suspend fun getCards (request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>>
}