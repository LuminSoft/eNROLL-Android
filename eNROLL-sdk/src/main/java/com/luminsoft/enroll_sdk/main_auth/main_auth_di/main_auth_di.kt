package com.luminsoft.enroll_sdk.main_auth.main_auth_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_api.MainAuthApi
import com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_remote_data_source.MainAuthRemoteDataSource
import com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_remote_data_source.MainAuthRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_repository.MainAuthRepositoryImplementation
import com.luminsoft.enroll_sdk.main_auth.main_auth_domain.repository.MainAuthRepository
import com.luminsoft.enroll_sdk.main_auth.main_auth_domain.usecases.GenerateAuthSessionTokenUsecase
import com.luminsoft.enroll_sdk.main_auth.main_auth_domain.usecases.GetAuthStepConfigurationsUsecase
import com.luminsoft.enroll_sdk.main_auth.main_auth_domain.usecases.InitializeRequestAuthUsecase
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainAuthModule = module {
    single {
        GenerateAuthSessionTokenUsecase(get())
    }
    single {
        GetAuthStepConfigurationsUsecase(get())
    }
    single {
        InitializeRequestAuthUsecase(get())
    }
    single<MainAuthRemoteDataSource> {
        MainAuthRemoteDataSourceImpl(get(), get())
    }
    single<MainAuthRepository> {
        MainAuthRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(MainAuthApi::class.java)
    }
    viewModel {
        AuthViewModel(get(), get(), get())
    }
//    viewModel {
//        LocationOnBoardingViewModel(get())
//    }

}