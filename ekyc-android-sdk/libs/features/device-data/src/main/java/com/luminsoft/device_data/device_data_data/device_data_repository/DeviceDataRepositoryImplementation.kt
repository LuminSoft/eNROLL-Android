package com.luminsoft.device_data.device_data_data.device_data_repository


import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.core.network.ApiBaseResponse
import com.luminsoft.core.network.BaseResponse
import com.luminsoft.device_data.device_data_data.device_data_models.get_token.GetCardsRequest
import com.luminsoft.device_data.device_data_data.device_data_models.get_token.TokenizedCardData
import com.luminsoft.device_data.device_data_data.device_data_remote_data_source.DeviceDataRemoteDataSource
import com.luminsoft.device_data.device_data_domain.repository.DeviceDataRepository

class DeviceDataRepositoryImplementation(private val DeviceDataRemoteDataSource: DeviceDataRemoteDataSource):
    DeviceDataRepository {

    override suspend fun getCards(request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        return when (val response = DeviceDataRemoteDataSource.getCards(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<ArrayList<TokenizedCardData>>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

