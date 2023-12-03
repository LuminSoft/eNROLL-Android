package com.luminsoft.main.main_di

import com.luminsoft.core.network.AuthInterceptor
import com.luminsoft.core.network.RetroClient
import com.luminsoft.main.main_data.main_api.MainApi
import com.luminsoft.main.main_data.main_remote_data_source.MainRemoteDataSource
import com.luminsoft.main.main_data.main_remote_data_source.MainRemoteDataSourceImpl
import com.luminsoft.main.main_data.main_repository.MainRepositoryImplementation
import com.luminsoft.main.main_domain.repository.MainRepository
import com.luminsoft.main.main_domain.usecases.GetSavedCardsUseCase
import com.luminsoft.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module{
    single {
        GetSavedCardsUseCase(get())
    }
    single<MainRemoteDataSource> {
        MainRemoteDataSourceImpl(get(),get())
    }
    single<MainRepository> {
        MainRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(MainApi::class.java)
    }
    viewModel{
        OnBoardingViewModel(get())
    }

}