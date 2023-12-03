package com.luminsoft.main.main_data.main_remote_data_source
import com.luminsoft.core.network.BaseRemoteDataSource
import com.luminsoft.core.network.BaseResponse
import com.luminsoft.main.main_data.main_models.get_token.GetCardsRequest
import com.luminsoft.main.main_data.main_api.MainApi


class  MainRemoteDataSourceImpl (private val network: BaseRemoteDataSource, private val mainApi: MainApi):
    MainRemoteDataSource {

    override suspend fun getCards(request: GetCardsRequest, ): BaseResponse<Any> {

            return network.apiRequest { mainApi.getCards(request) }

    }
}






