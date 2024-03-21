package com.luminsoft.enroll_sdk.features.email.email_data.email_remote_data_source
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.get_token.GetCardsRequest

interface  EmailRemoteDataSource  {
    suspend fun getCards(request: GetCardsRequest): BaseResponse<Any>
}