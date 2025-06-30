package com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_di

import PhoneAuthUpdateApi
import PhoneAuthUpdateRemoteDataSource
import PhoneAuthUpdateRemoteDataSourceImpl
import PhoneAuthUpdateRepository
import PhoneAuthUpdateRepositoryImplementation
import PhoneAuthUpdateSendOTPUseCase
import PhoneAuthUpdateViewModel
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val phoneAuthUpdateModule = module {
    single {
        PhoneAuthUpdateSendOTPUseCase(get())
    }
    single<PhoneAuthUpdateRemoteDataSource> {
        PhoneAuthUpdateRemoteDataSourceImpl(get(), get())
    }
    single<PhoneAuthUpdateRepository> {
        PhoneAuthUpdateRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(PhoneAuthUpdateApi::class.java)
    }
    viewModel {
        PhoneAuthUpdateViewModel(get(), get())
    }


}