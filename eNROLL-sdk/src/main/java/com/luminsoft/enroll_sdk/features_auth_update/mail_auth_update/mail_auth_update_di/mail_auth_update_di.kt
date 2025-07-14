package com.luminsoft.enroll_sdk.features_auth_update.mail_auth_update.mail_auth_update_di

import MailAuthUpdateApi
import MailAuthUpdateRemoteDataSource
import MailAuthUpdateRemoteDataSourceImpl
import MailAuthUpdateRepository
import MailAuthUpdateRepositoryImplementation
import MailAuthUpdateSendOTPUseCase
import MailAuthUpdateViewModel
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mailAuthUpdateModule = module {
    single {
        MailAuthUpdateSendOTPUseCase(get())
    }
    single<MailAuthUpdateRemoteDataSource> {
        MailAuthUpdateRemoteDataSourceImpl(get(), get())
    }
    single<MailAuthUpdateRepository> {
        MailAuthUpdateRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(MailAuthUpdateApi::class.java)
    }
    viewModel {
        MailAuthUpdateViewModel(get(), get())
    }


}