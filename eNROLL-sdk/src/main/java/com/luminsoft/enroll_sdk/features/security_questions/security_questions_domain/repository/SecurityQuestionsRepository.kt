package com.luminsoft.enroll_sdk.features.security_questions.security_questions_domain.repository

import arrow.core.Either
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.get_token.GetCardsRequest
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.get_token.TokenizedCardData

interface SecurityQuestionsRepository {
     suspend fun getCards (request: GetCardsRequest): Either<com.luminsoft.enroll_sdk.core.failures.SdkFailure, ArrayList<TokenizedCardData>>
}