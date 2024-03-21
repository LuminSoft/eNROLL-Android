package com.luminsoft.enroll_sdk.features.device_data.device_data_domain.repository

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.device_data.device_data_data.device_data_models.get_token.GetCardsRequest
import com.luminsoft.enroll_sdk.features.device_data.device_data_data.device_data_models.get_token.TokenizedCardData

interface DeviceDataRepository {
     suspend fun getCards (request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>>
}