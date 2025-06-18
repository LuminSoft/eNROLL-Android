package com.luminsoft.enroll_sdk.features_forget.forget_password.forget_password_di

import ForgetPasswordApi
import ForgetPasswordRemoteDataSource
import ForgetPasswordRemoteDataSourceImpl
import ForgetPasswordRepository
import ForgetPasswordRepositoryImplementation
import ForgetPasswordUseCase
import ForgetPasswordViewModel
import GetDefaultEmailUseCase
import MailSendOTPUseCase
import ValidateOtpMailUseCase
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val forgetPasswordModule = module {
    single {
        ForgetPasswordUseCase(get())
    }
    single {
        GetDefaultEmailUseCase(get())
    }

    single {
        MailSendOTPUseCase(get())
    }

    single {
        ValidateOtpMailUseCase(get())
    }


    single<ForgetPasswordRemoteDataSource> {
        ForgetPasswordRemoteDataSourceImpl(get(), get())
    }


    single<ForgetPasswordRepository> {
        ForgetPasswordRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(ForgetPasswordApi::class.java)
    }
    viewModel {
        ForgetPasswordViewModel(get(), get(), get(), get())
    }


}