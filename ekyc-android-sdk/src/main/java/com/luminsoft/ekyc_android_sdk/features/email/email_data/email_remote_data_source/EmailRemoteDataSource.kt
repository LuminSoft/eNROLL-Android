package com.luminsoft.ekyc_android_sdk.features.email.email_data.email_remote_data_source
import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.email.email_data.email_models.get_token.GetCardsRequest

interface  EmailRemoteDataSource  {
    suspend fun getCards(request: GetCardsRequest): BaseResponse<Any>
}