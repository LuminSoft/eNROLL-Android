package com.luminsoft.ekyc_android_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_remote_data_source

import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.get_token.GetCardsRequest

interface  PhoneNumbersRemoteDataSource  {
    suspend fun getCards(request: GetCardsRequest): BaseResponse<Any>
}