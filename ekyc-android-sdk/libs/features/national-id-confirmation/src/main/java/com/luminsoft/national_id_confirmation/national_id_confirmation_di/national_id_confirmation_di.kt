package com.luminsoft.national_id_confirmation.national_id_confirmation_di

import com.luminsoft.core.network.AuthInterceptor
import com.luminsoft.core.network.RetroClient
import com.luminsoft.national_id_confirmation.national_id_confirmation_domain.usecases.GetSavedCardsUseCase
import com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_api.NationalIdConfirmationApi
import com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_remote_data_source.NationalIdConfirmationRemoteDataSource
import com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_remote_data_source.NationalIdConfirmationRemoteDataSourceImpl
import com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_repository.NationalIdConfirmationRepositoryImplementation
import com.luminsoft.national_id_confirmation.national_id_confirmation_domain.repository.NationalIdConfirmationRepository
import com.luminsoft.national_id_confirmation.national_id_confirmation_presentation.national_id_onboarding.view_model.NationalIdFrontOcrViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val nationalIdConfirmationModule = module{
    single {
        GetSavedCardsUseCase(get())
    }
    single<NationalIdConfirmationRemoteDataSource> {
        NationalIdConfirmationRemoteDataSourceImpl(get(),get())
    }
    single<NationalIdConfirmationRepository> {
        NationalIdConfirmationRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(NationalIdConfirmationApi::class.java)
    }
    viewModel{
        NationalIdFrontOcrViewModel(/*get(),get()*/)
    }

}