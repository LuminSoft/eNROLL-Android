package com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_repository


import arrow.core.Either
import com.luminsoft.core.failures.SdkFailure
import com.luminsoft.core.network.ApiBaseResponse
import com.luminsoft.core.network.BaseResponse
import com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.get_token.GetCardsRequest
import com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.get_token.TokenizedCardData
import com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_remote_data_source.NationalIdConfirmationRemoteDataSource
import com.luminsoft.national_id_confirmation.national_id_confirmation_domain.repository.NationalIdConfirmationRepository

class NationalIdConfirmationRepositoryImplementation(private val nationalIdConfirmationRemoteDataSource: NationalIdConfirmationRemoteDataSource):
    NationalIdConfirmationRepository {

    override suspend fun getCards(request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        return when (val response = nationalIdConfirmationRemoteDataSource.getCards(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<ArrayList<TokenizedCardData>>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

