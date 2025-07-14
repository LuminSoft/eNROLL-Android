package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_api.PhoneNumbersApi
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_remote_data_source.PhoneNumbersRemoteDataSource
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_remote_data_source.PhoneNumbersRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_repository.PhoneNumbersRepositoryImplementation
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.repository.PhoneNumbersRepository
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.GetCountriesUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val phoneNumbersModule = module {
    single {
        GetCountriesUseCase(get())
    }
    single<PhoneNumbersRemoteDataSource> {
        PhoneNumbersRemoteDataSourceImpl(get(), get())
    }
    single<PhoneNumbersRepository> {
        PhoneNumbersRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(PhoneNumbersApi::class.java)
    }

}