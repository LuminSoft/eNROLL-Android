package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.get_token.GetCardsRequest

interface  PhoneNumbersRemoteDataSource  {
    suspend fun getCards(request: GetCardsRequest): BaseResponse<Any>
}