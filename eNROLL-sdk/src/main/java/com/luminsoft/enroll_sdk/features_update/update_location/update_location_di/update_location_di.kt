package com.luminsoft.enroll_sdk.features_update.update_location.update_location_di

import UpdateLocationRemoteDataSource
import UpdateLocationRemoteDataSourceImpl
import UpdateLocationRepository
import UpdateLocationRepositoryImplementation
import UpdateLocationUseCase
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_update.update_location.update_location_data.update_location_api.UpdateLocationApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val updateLocationModule = module {
    single {
        UpdateLocationUseCase(get())
    }
    single<UpdateLocationRemoteDataSource> {
        UpdateLocationRemoteDataSourceImpl(get(), get())
    }
    single<UpdateLocationRepository> {
        UpdateLocationRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(UpdateLocationApi::class.java)
    }

}