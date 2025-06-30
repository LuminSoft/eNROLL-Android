package com.luminsoft.enroll_sdk.features_update.update_password.update_password_di

import UpdatePasswordApi
import UpdatePasswordRemoteDataSource
import UpdatePasswordRemoteDataSourceImpl
import UpdatePasswordRepository
import UpdatePasswordRepositoryImplementation
import UpdatePasswordUseCase
import UpdatePasswordViewModel
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val updatePasswordModule = module {
    single {
        UpdatePasswordUseCase(get())
    }
    single<UpdatePasswordRemoteDataSource> {
        UpdatePasswordRemoteDataSourceImpl(get(), get())
    }
    single<UpdatePasswordRepository> {
        UpdatePasswordRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(UpdatePasswordApi::class.java)
    }
    viewModel {
        UpdatePasswordViewModel(get())
    }


}