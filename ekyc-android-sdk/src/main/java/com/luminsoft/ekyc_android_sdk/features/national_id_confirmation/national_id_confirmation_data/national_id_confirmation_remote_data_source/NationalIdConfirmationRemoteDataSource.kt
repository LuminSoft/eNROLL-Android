package com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_remote_data_source

import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.get_token.GetCardsRequest

interface  NationalIdConfirmationRemoteDataSource  {
    suspend fun getCards(request: GetCardsRequest): BaseResponse<Any>
}