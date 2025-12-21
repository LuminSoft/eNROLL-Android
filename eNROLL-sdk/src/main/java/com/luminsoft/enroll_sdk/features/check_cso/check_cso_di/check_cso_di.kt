package com.luminsoft.enroll_sdk.features.check_cso.check_cso_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_api.CheckCsoApi
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_remote_data_source.CheckCsoRemoteDataSource
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_remote_data_source.CheckCsoRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_repository.CheckCsoRepositoryImplementation
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_domain.repository.CheckCsoRepository
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_domain.usecases.CheckCsoUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val checkCsoModule = module {
    single {
        CheckCsoUseCase(get())
    }
    single<CheckCsoRemoteDataSource> {
        CheckCsoRemoteDataSourceImpl(get(), get())
    }
    single<CheckCsoRepository> {
        CheckCsoRepositoryImplementation(get())
    }
    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(CheckCsoApi::class.java)
    }

}
