package com.luminsoft.enroll_sdk.main.main_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.location.location_onboarding.view_model.LocationOnBoardingViewModel
import com.luminsoft.enroll_sdk.main.main_data.main_api.MainApi
import com.luminsoft.enroll_sdk.main.main_data.main_remote_data_source.MainRemoteDataSource
import com.luminsoft.enroll_sdk.main.main_data.main_remote_data_source.MainRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.main.main_data.main_repository.MainRepositoryImplementation
import com.luminsoft.enroll_sdk.main.main_domain.repository.MainRepository
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GenerateOnboardingSessionTokenUsecase
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GetApplicantIdUsecase
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GetCurrentStepUsecase
import com.luminsoft.enroll_sdk.main.main_domain.usecases.GetOnboardingStepConfigurationsUsecase
import com.luminsoft.enroll_sdk.main.main_domain.usecases.InitializeRequestUsecase
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single {
        GenerateOnboardingSessionTokenUsecase(get())
    }
    single {
        GetOnboardingStepConfigurationsUsecase(get())
    }
    single {
        InitializeRequestUsecase(get())
    }
    single {
        GetApplicantIdUsecase(get())
    }
    single {
        GetCurrentStepUsecase(get())
    }
    single<MainRemoteDataSource> {
        MainRemoteDataSourceImpl(get(), get())
    }
    single<MainRepository> {
        MainRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(MainApi::class.java)
    }
    viewModel {
        OnBoardingViewModel(get(), get(), get(), get(), get(), context = androidApplication())
    }
    viewModel {
        LocationOnBoardingViewModel(get())
    }
}