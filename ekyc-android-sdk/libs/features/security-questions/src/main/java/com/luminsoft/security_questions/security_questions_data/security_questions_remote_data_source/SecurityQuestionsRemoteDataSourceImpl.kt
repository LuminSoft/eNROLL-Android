package com.luminsoft.security_questions.security_questions_data.security_questions_remote_data_source
import com.luminsoft.core.network.BaseRemoteDataSource
import com.luminsoft.core.network.BaseResponse
import com.luminsoft.security_questions.security_questions_data.security_questions_models.get_token.GetCardsRequest
import com.luminsoft.security_questions.security_questions_data.security_questions_api.SecurityQuestionsApi


class  SecurityQuestionsRemoteDataSourceImpl (private val network: BaseRemoteDataSource, private val securityQuestionsApi: SecurityQuestionsApi):
    SecurityQuestionsRemoteDataSource {

    override suspend fun getCards(request: GetCardsRequest, ): BaseResponse<Any> {

            return network.apiRequest { securityQuestionsApi.getCards(request) }

    }
}






