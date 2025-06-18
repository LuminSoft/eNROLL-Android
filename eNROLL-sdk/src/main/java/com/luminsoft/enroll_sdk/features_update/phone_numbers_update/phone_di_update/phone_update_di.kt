package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_di_update

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_api_update.PhoneApiUpdate
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_remote_data_source_update.PhoneRemoteDataSourceUpdate
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_remote_data_source_update.PhoneRemoteDataSourceUpdateImpl
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_repository_update.PhoneRepositoryUpdateImplementation
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.repository.PhoneRepositoryUpdate
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val phoneUpdateModule = module {

    single<PhoneRemoteDataSourceUpdate> {
        PhoneRemoteDataSourceUpdateImpl(get(), get())
    }
    single<PhoneRepositoryUpdate> {
        PhoneRepositoryUpdateImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(PhoneApiUpdate::class.java)
    }
}