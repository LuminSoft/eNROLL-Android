package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_remote_data_source
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.get_token.GetCardsRequest
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_api.PhoneNumbersApi

class  PhoneNumbersRemoteDataSourceImpl (private val network: BaseRemoteDataSource, private val phoneNumbersApi: PhoneNumbersApi):
    PhoneNumbersRemoteDataSource {

    override suspend fun getCards(request: GetCardsRequest, ): BaseResponse<Any> {

            return network.apiRequest { phoneNumbersApi.getCards(request) }

    }
}






