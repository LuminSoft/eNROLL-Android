package com.luminsoft.phone_numbers.phone_numbers_domain.repository

import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.phone_numbers.phone_numbers_data.phone_numbers_models.get_token.GetCardsRequest
import com.luminsoft.phone_numbers.phone_numbers_data.phone_numbers_models.get_token.TokenizedCardData

interface PhoneNumbersRepository {
     suspend fun getCards (request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>>
}