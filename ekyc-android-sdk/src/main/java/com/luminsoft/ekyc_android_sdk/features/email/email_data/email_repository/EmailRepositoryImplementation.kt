package com.luminsoft.ekyc_android_sdk.features.email.email_data.email_repository


import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.network.ApiBaseResponse
import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.email.email_domain.repository.EmailRepository
import com.luminsoft.ekyc_android_sdk.features.email.email_data.email_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.email.email_data.email_models.get_token.TokenizedCardData
import com.luminsoft.ekyc_android_sdk.features.email.email_data.email_remote_data_source.EmailRemoteDataSource

class EmailRepositoryImplementation(private val emailRemoteDataSource: EmailRemoteDataSource):
    EmailRepository {

    override suspend fun getCards(request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        return when (val response = emailRemoteDataSource.getCards(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<ArrayList<TokenizedCardData>>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

