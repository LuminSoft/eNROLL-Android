package com.luminsoft.enroll_sdk.features_auth_update.device_id_auth_update.device_id_auth_update_di

import DeviceIdAuthUpdateRemoteDataSource
import DeviceIdAuthUpdateRemoteDataSourceImpl
import DeviceIdAuthUpdateRepository
import DeviceIdAuthUpdateRepositoryImplementation
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_auth_update.device_id_auth_update.device_id_auth_update_data.device_id_auth_update_api.DeviceIdAuthUpdateApi
import com.luminsoft.enroll_sdk.features_auth_update.device_id_auth_update.device_id_auth_update_domain.usecases.CheckDeviceIdAuthUpdateUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val deviceIdAuthUpdateModule = module {
    single {
        CheckDeviceIdAuthUpdateUseCase(get())
    }
    single<DeviceIdAuthUpdateRemoteDataSource> {
        DeviceIdAuthUpdateRemoteDataSourceImpl(get(), get())
    }
    single<DeviceIdAuthUpdateRepository> {
        DeviceIdAuthUpdateRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(DeviceIdAuthUpdateApi::class.java)
    }
}