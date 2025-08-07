package com.luminsoft.enroll_sdk.features_auth_update.password_auth_update.password_auth_update_di

import PasswordAuthUpdateApi
import PasswordAuthUpdateRemoteDataSource
import PasswordAuthUpdateRemoteDataSourceImpl
import PasswordAuthUpdateRepository
import PasswordAuthUpdateRepositoryImplementation
import PasswordAuthUpdateUseCase
import PasswordAuthUpdateViewModel
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val passwordAuthUpdateModule = module {
    single {
        PasswordAuthUpdateUseCase(get())
    }
    single<PasswordAuthUpdateRemoteDataSource> {
        PasswordAuthUpdateRemoteDataSourceImpl(get(), get())
    }
    single<PasswordAuthUpdateRepository> {
        PasswordAuthUpdateRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(PasswordAuthUpdateApi::class.java)
    }
    viewModel {
        PasswordAuthUpdateViewModel(get())
    }


}