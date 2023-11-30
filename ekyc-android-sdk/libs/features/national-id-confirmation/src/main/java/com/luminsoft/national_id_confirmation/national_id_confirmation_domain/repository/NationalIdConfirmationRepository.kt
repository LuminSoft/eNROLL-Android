package com.luminsoft.national_id_confirmation.national_id_confirmation_domain.repository

import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.get_token.GetCardsRequest
import com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.get_token.TokenizedCardData

interface NationalIdConfirmationRepository {
     suspend fun getCards (request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>>
}