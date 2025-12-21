package com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_api.CheckNtraApi
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_remote_data_source.CheckNtraRemoteDataSource
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_remote_data_source.CheckNtraRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_repository.CheckNtraRepositoryImplementation
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_domain.repository.CheckNtraRepository
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_domain.usecases.CheckNtraUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val checkNtraModule = module {
    single {
        CheckNtraUseCase(get())
    }
    single<CheckNtraRemoteDataSource> {
        CheckNtraRemoteDataSourceImpl(get(), get())
    }
    single<CheckNtraRepository> {
        CheckNtraRepositoryImplementation(get())
    }
    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(CheckNtraApi::class.java)
    }

}
