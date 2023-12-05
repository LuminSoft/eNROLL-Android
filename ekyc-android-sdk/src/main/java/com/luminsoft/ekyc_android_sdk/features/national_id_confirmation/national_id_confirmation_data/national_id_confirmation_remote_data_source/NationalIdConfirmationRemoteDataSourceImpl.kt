package com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_remote_data_source
import com.luminsoft.ekyc_android_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_api.NationalIdConfirmationApi


class  NationalIdConfirmationRemoteDataSourceImpl (private val network:BaseRemoteDataSource, private val nationalIdConfirmationApi: NationalIdConfirmationApi):
    NationalIdConfirmationRemoteDataSource {

    override suspend fun getCards(request: GetCardsRequest): BaseResponse<Any> {
            return network.apiRequest { nationalIdConfirmationApi.getCards(request) }
    }
}






