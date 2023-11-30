package com.luminsoft.password.password_di

import com.luminsoft.core.network.AuthInterceptor
import com.luminsoft.core.network.RetroClient
import com.luminsoft.password.password_domain.usecases.GetSavedCardsUseCase
import com.luminsoft.password.password_data.password_api.PasswordApi
import com.luminsoft.password.password_data.password_remote_data_source.PasswordRemoteDataSource
import com.luminsoft.password.password_data.password_remote_data_source.PasswordRemoteDataSourceImpl
import com.luminsoft.password.password_data.password_repository.PasswordRepositoryImplementation
import com.luminsoft.password.password_domain.repository.PasswordRepository
import com.luminsoft.password.password_presentation.password_onboarding.view_model.PasswordOnBoardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val passwordModule = module{
    single {
        GetSavedCardsUseCase(get())
    }
    single<PasswordRemoteDataSource> {
        PasswordRemoteDataSourceImpl(get(),get())
    }
    single<PasswordRepository> {
        PasswordRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(PasswordApi::class.java)
    }
    viewModel{
        PasswordOnBoardingViewModel(get())
    }


}