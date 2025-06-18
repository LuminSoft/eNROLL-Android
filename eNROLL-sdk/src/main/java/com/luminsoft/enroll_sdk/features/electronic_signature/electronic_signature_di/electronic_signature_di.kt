package com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_di

import ElectronicSignatureRemoteDataSource
import ElectronicSignatureRemoteDataSourceImpl
import ElectronicSignatureRepository
import ElectronicSignatureRepositoryImplementation
import InsertSignatureInfoUseCase
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_data.electronic_signature_api.ElectronicSignatureApi
import com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_domain.usecases.CheckUserHasNationalIdUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val electronicSignatureModule = module {
    single {
        InsertSignatureInfoUseCase(get())
    }
    single {
        CheckUserHasNationalIdUseCase(get())
    }
    single<ElectronicSignatureRemoteDataSource> {
        ElectronicSignatureRemoteDataSourceImpl(get(), get())
    }
    single<ElectronicSignatureRepository> {
        ElectronicSignatureRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(ElectronicSignatureApi::class.java)
    }

}