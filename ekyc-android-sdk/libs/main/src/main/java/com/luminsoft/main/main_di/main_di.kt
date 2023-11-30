package com.luminsoft.cowpay_sdk.features.cards_payment.di

import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_domain.repository.CardsPaymentRepository
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_domain.usecases.GetSavedCardsUseCase
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.add_card.view_model.AddCardViewModel
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.saved_cards.view_model.SavedCardsViewModel
import com.luminsoft.cowpay_sdk.network.AuthInterceptor
import com.luminsoft.cowpay_sdk.network.RetroClient
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_api.CardsPaymentApi
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_repository.CardsPaymentRepositoryImplementation
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_remote_data_source.CardsPaymentRemoteDataSource
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_remote_data_source.CardsPaymentRemoteDataSourceImpl
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.web_view.view_models.WebViewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cardsPaymentModule = module{
    single {
        GetSavedCardsUseCase(get())
    }
    single<CardsPaymentRemoteDataSource> {
        CardsPaymentRemoteDataSourceImpl(get(),get())
    }
    single<CardsPaymentRepository> {
        CardsPaymentRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(CardsPaymentApi::class.java)
    }
    viewModel{
        AddCardViewModel(get(),get())
    }
    viewModel{
        SavedCardsViewModel(get(),get(),get())
    }
    viewModel{
        WebViewViewModel()
    }

}