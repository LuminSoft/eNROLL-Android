package com.luminsoft.enroll_sdk.features.check_aml.check_aml_di

import CheckAmlUseCase
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.check_aml.check_aml_data.check_aml_api.CheckAmlApi
import com.luminsoft.enroll_sdk.features.check_aml.check_aml_data.check_aml_remote_data_source.CheckAmlRemoteDataSource
import com.luminsoft.enroll_sdk.features.check_aml.check_aml_data.check_aml_remote_data_source.CheckAmlRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features.check_aml.check_aml_data.check_aml_repository.CheckAmlRepositoryImplementation
import com.luminsoft.enroll_sdk.features.check_aml.check_aml_domain.repository.CheckAmlRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val checkAmlModule = module {
    single {
        CheckAmlUseCase(get())
    }
    single<CheckAmlRemoteDataSource> {
        CheckAmlRemoteDataSourceImpl(get(), get())
    }
    single<CheckAmlRepository> {
        CheckAmlRepositoryImplementation(get())
    }
    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(CheckAmlApi::class.java)
    }

}