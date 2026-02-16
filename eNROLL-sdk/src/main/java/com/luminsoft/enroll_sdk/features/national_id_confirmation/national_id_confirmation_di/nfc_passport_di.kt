package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_api.NfcPassportApi
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_remote_data_source.NfcPassportRemoteDataSource
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_remote_data_source.NfcPassportRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_repository.NfcPassportRepositoryImplementation
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.repository.NfcPassportRepository
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.UploadNfcPassportUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val nfcPassportModule = module {
    single {
        UploadNfcPassportUseCase(get())
    }
    single<NfcPassportRemoteDataSource> {
        NfcPassportRemoteDataSourceImpl(get(), get())
    }
    single<NfcPassportRepository> {
        NfcPassportRepositoryImplementation(get())
    }
    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(NfcPassportApi::class.java)
    }
}
