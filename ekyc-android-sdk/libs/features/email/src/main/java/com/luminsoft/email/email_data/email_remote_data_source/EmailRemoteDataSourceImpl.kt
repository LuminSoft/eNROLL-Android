package com.luminsoft.email.email_data.email_remote_data_source
import com.luminsoft.core.network.BaseRemoteDataSource
import com.luminsoft.core.network.BaseResponse
import com.luminsoft.email.email_data.email_models.get_token.GetCardsRequest
import com.luminsoft.email.email_data.email_api.EmailApi


class  EmailRemoteDataSourceImpl (private val network: BaseRemoteDataSource, private val emailApi: EmailApi):
    EmailRemoteDataSource {

    override suspend fun getCards(request: GetCardsRequest, ): BaseResponse<Any> {

            return network.apiRequest { emailApi.getCards(request) }

    }
}






