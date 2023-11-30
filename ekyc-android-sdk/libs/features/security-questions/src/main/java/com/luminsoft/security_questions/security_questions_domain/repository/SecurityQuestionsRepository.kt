package com.luminsoft.security_questions.security_questions_domain.repository

import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.security_questions.security_questions_data.security_questions_models.get_token.GetCardsRequest
import com.luminsoft.security_questions.security_questions_data.security_questions_models.get_token.TokenizedCardData

interface SecurityQuestionsRepository {
     suspend fun getCards (request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>>
}