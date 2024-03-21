package com.luminsoft.enroll_sdk.features.device_data.device_data_data.device_data_repository


import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.ApiBaseResponse
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.device_data.device_data_data.device_data_models.get_token.GetCardsRequest
import com.luminsoft.enroll_sdk.features.device_data.device_data_data.device_data_models.get_token.TokenizedCardData
import com.luminsoft.enroll_sdk.features.device_data.device_data_data.device_data_remote_data_source.DeviceDataRemoteDataSource
import com.luminsoft.enroll_sdk.features.device_data.device_data_domain.repository.DeviceDataRepository

class DeviceDataRepositoryImplementation(private val deviceDataRemoteDataSource: DeviceDataRemoteDataSource):
    DeviceDataRepository {

    override suspend fun getCards(request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        return when (val response = deviceDataRemoteDataSource.getCards(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<ArrayList<TokenizedCardData>>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

