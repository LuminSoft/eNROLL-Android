package com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_di

import PhoneAuthRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth.view_model.PhoneAuthViewModel
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_data.phone_auth_api.PhoneAuthApi
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_data.phone_auth_remote_data_source.PhoneAuthRemoteDataSource
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_data.phone_auth_repository.PhoneAuthRepositoryImplementation
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_domain.repository.PhoneAuthRepository
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_domain.usecases.PhoneAuthSendOTPUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val phoneAuthModule = module {
    single {
        PhoneAuthSendOTPUseCase(get())
    }
    single<PhoneAuthRemoteDataSource> {
        PhoneAuthRemoteDataSourceImpl(get(), get())
    }
    single<PhoneAuthRepository> {
        PhoneAuthRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(PhoneAuthApi::class.java)
    }
    viewModel {
        PhoneAuthViewModel(get(), get())
    }


}