package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_repository


import arrow.core.Either
import com.luminsoft.cowpay_sdk.failures.SdkFailure
import com.luminsoft.cowpay_sdk.network.BaseResponse
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.get_token.GetCardsRequest
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.get_token.TokenizedCardData
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_remote_data_source.CardsPaymentRemoteDataSource
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_domain.repository.CardsPaymentRepository
import com.luminsoft.cowpay_sdk.network.ApiBaseResponse

class MainRepositoryImplementation(private val cardsPaymentRemoteDataSource: CardsPaymentRemoteDataSource):
    CardsPaymentRepository {

    override suspend fun getCards(request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        return when (val response = cardsPaymentRemoteDataSource.getCards(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<ArrayList<TokenizedCardData>>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

