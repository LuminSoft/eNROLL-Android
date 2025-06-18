package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_api.MainForgetApi
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_remote_data_source.MainForgetRemoteDataSource
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_remote_data_source.MainForgetRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_repository.MainForgetRepositoryImplementation
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.repository.MainForgetRepository
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases.ForgetStepsConfigurationsUsecase
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases.GenerateForgetSessionForStepTokenUsecase
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases.GenerateForgetSessionTokenUsecase
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases.InitializeForgetRequestUsecase
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.usecases.VerifyPasswordUsecase
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.main_forget.view_model.ForgetViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainForgetModule = module {
    single {
        GenerateForgetSessionTokenUsecase(get())
    }
    single {
        ForgetStepsConfigurationsUsecase(get())
    }
    single {
        GenerateForgetSessionForStepTokenUsecase(get())
    }

    single {
        VerifyPasswordUsecase(get())
    }
    single {
        InitializeForgetRequestUsecase(get())
    }


    single<MainForgetRemoteDataSource> {
        MainForgetRemoteDataSourceImpl(get(), get())
    }
    single<MainForgetRepository> {
        MainForgetRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(MainForgetApi::class.java)
    }
    viewModel {
        ForgetViewModel(
            get(), get(), get(), get(), get()
        )
    }


}