package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_repository


import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.ApiBaseResponse
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.get_token.GetCardsRequest
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.get_token.TokenizedCardData
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.repository.PhoneNumbersRepository
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_remote_data_source.PhoneNumbersRemoteDataSource

class PhoneNumbersRepositoryImplementation(private val phoneNumbersRemoteDataSource: PhoneNumbersRemoteDataSource):
    PhoneNumbersRepository {

    override suspend fun getCards(request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        return when (val response = phoneNumbersRemoteDataSource.getCards(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<ArrayList<TokenizedCardData>>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

