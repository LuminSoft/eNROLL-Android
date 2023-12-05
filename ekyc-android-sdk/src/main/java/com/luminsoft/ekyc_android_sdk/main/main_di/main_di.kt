package com.luminsoft.ekyc_android_sdk.main.main_di

import com.luminsoft.ekyc_android_sdk.core.network.AuthInterceptor
import com.luminsoft.ekyc_android_sdk.core.network.RetroClient
import com.luminsoft.ekyc_android_sdk.main.main_data.main_api.MainApi
import com.luminsoft.ekyc_android_sdk.main.main_data.main_remote_data_source.MainRemoteDataSource
import com.luminsoft.ekyc_android_sdk.main.main_data.main_remote_data_source.MainRemoteDataSourceImpl
import com.luminsoft.ekyc_android_sdk.main.main_data.main_repository.MainRepositoryImplementation
import com.luminsoft.ekyc_android_sdk.main.main_domain.repository.MainRepository
import com.luminsoft.ekyc_android_sdk.main.main_domain.usecases.GenerateOnboardingSessionTokenUsecase
import com.luminsoft.ekyc_android_sdk.main.main_onboarding.view_model.AuthViewModel
import com.luminsoft.ekyc_android_sdk.main.main_onboarding.view_model.OnBoardingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module{
    single {
        GenerateOnboardingSessionTokenUsecase(get())
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
    viewModel{
        AuthViewModel(get())
    }

}