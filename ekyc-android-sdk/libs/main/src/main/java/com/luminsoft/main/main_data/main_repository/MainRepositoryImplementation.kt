package com.luminsoft.main.main_data.main_repository


import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.core.network.ApiBaseResponse
import com.luminsoft.core.network.BaseResponse
import com.luminsoft.main.main_domain.repository.MainRepository
import com.luminsoft.main.main_data.main_models.get_token.GetCardsRequest
import com.luminsoft.main.main_data.main_models.get_token.TokenizedCardData
import com.luminsoft.main.main_data.main_remote_data_source.MainRemoteDataSource

class MainRepositoryImplementation(private val mainRemoteDataSource: MainRemoteDataSource):
    MainRepository {

    override suspend fun getCards(request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        return when (val response = mainRemoteDataSource.getCards(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<ArrayList<TokenizedCardData>>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

