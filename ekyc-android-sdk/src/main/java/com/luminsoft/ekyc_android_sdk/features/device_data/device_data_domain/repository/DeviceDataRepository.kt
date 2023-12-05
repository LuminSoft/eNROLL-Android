package com.luminsoft.ekyc_android_sdk.features.device_data.device_data_domain.repository

import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_data.device_data_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_data.device_data_models.get_token.TokenizedCardData

interface DeviceDataRepository {
     suspend fun getCards (request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>>
}