package com.luminsoft.enroll_sdk.features_update.update_device_id.update_device_id_di

import UpdateDeviceIdApi
import UpdateDeviceIdRemoteDataSource
import UpdateDeviceIdRemoteDataSourceImpl
import UpdateDeviceIdRepository
import UpdateDeviceIdRepositoryImplementation
import UpdateDeviceIdUseCase
import UpdateDeviceIdViewModel
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val updateDeviceIdModule = module {
    single {
        UpdateDeviceIdUseCase(get())
    }
    single<UpdateDeviceIdRemoteDataSource> {
        UpdateDeviceIdRemoteDataSourceImpl(get(), get())
    }
    single<UpdateDeviceIdRepository> {
        UpdateDeviceIdRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(UpdateDeviceIdApi::class.java)
    }
    viewModel {
        UpdateDeviceIdViewModel(get(), context = androidContext())
    }
}