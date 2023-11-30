package com.luminsoft.phone_numbers.phone_numbers_data.phone_numbers_remote_data_source
import com.luminsoft.core.network.BaseRemoteDataSource
import com.luminsoft.core.network.BaseResponse
import com.luminsoft.phone_numbers.phone_numbers_data.phone_numbers_models.get_token.GetCardsRequest
import com.luminsoft.phone_numbers.phone_numbers_data.phone_numbers_api.PhoneNumbersApi

class  PhoneNumbersRemoteDataSourceImpl (private val network: BaseRemoteDataSource, private val phoneNumbersApi: PhoneNumbersApi):
    PhoneNumbersRemoteDataSource {

    override suspend fun getCards(request: GetCardsRequest, ): BaseResponse<Any> {

            return network.apiRequest { phoneNumbersApi.getCards(request) }

    }
}






