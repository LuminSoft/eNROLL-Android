package com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.get_token.GetCardsRequest

interface  SecurityQuestionsRemoteDataSource  {
    suspend fun getCards(request: GetCardsRequest): BaseResponse<Any>
}