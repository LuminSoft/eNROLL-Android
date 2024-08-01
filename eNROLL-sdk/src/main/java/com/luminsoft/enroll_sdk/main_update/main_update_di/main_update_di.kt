package com.luminsoft.enroll_sdk.main_update.main_update_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_api.MainUpdateApi
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_remote_data_source.MainUpdateRemoteDataSource
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_remote_data_source.MainUpdateRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_repository.MainUpdateRepositoryImplementation
import com.luminsoft.enroll_sdk.main_update.main_update_domain.repository.MainUpdateRepository
import com.luminsoft.enroll_sdk.main_update.main_update_domain.usecases.GenerateUpdateSessionTokenUsecase
import com.luminsoft.enroll_sdk.main_update.main_update_domain.usecases.UpdateStepsConfigurationsUsecase
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainUpdateModule = module {
    single {
        GenerateUpdateSessionTokenUsecase(get())
    }
    single {
        UpdateStepsConfigurationsUsecase(get())
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