package com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_data.check_imei_auth_api.CheckIMEIAuthApi
import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_data.check_imei_auth_remote_data_source.CheckIMEIAuthRemoteDataSource
import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_data.check_imei_auth_remote_data_source.CheckIMEIAuthRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_data.check_imei_auth_repository.CheckIMEIAuthRepositoryImplementation
import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_domain.repository.CheckIMEIAuthRepository
import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_domain.usecases.AuthCheckIMEIUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val checkIMEIAuthModule = module {
    single {
        AuthCheckIMEIUseCase(get())
    }
    single<CheckIMEIAuthRemoteDataSource> {
        CheckIMEIAuthRemoteDataSourceImpl(get(), get())
    }
    single<CheckIMEIAuthRepository> {
        CheckIMEIAuthRepositoryImplementation(get())
    }
    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(CheckIMEIAuthApi::class.java)
    }
}