package com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth.view_model.PasswordAuthViewModel
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_data.password_auth_api.PasswordAuthApi
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_data.password_auth_remote_data_source.PasswordAuthRemoteDataSource
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_data.password_auth_remote_data_source.PasswordAuthRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_data.password_auth_repository.PasswordAuthRepositoryImplementation
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_domain.repository.PasswordAuthRepository
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_domain.usecases.PasswordAuthUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val passwordAuthModule = module {
    single {
        PasswordAuthUseCase(get())
    }
    single<PasswordAuthRemoteDataSource> {
        PasswordAuthRemoteDataSourceImpl(get(), get())
    }
    single<PasswordAuthRepository> {
        PasswordAuthRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(PasswordAuthApi::class.java)
    }
    viewModel {
        PasswordAuthViewModel(get())
    }


}