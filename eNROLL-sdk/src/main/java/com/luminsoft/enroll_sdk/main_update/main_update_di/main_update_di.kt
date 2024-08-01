package com.luminsoft.enroll_sdk.main_update.main_update_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.main_auth.main_auth_domain.usecases.GenerateAuthSessionTokenUsecase
import com.luminsoft.enroll_sdk.main_auth.main_auth_domain.usecases.GetAuthStepConfigurationsUsecase
import com.luminsoft.enroll_sdk.main_auth.main_auth_domain.usecases.InitializeRequestAuthUsecase
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_api.MainUpdateApi
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_remote_data_source.MainUpdateRemoteDataSource
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_remote_data_source.MainUpdateRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_repository.MainUpdateRepositoryImplementation
import com.luminsoft.enroll_sdk.main_update.main_update_domain.repository.MainUpdateRepository
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_auth.view_model.UpdateViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainUpdateModule = module {
    single {
        GenerateAuthSessionTokenUsecase(get())
    }
    single {
        GetAuthStepConfigurationsUsecase(get())
    }
    single {
        InitializeRequestAuthUsecase(get())
    }
    single<MainUpdateRemoteDataSource> {
        MainUpdateRemoteDataSourceImpl(get(), get())
    }
    single<MainUpdateRepository> {
        MainUpdateRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(MainUpdateApi::class.java)
    }
    viewModel {
        UpdateViewModel(
            get(), get(), context = androidApplication()
        )
    }


}