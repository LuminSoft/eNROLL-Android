package com.luminsoft.enroll_sdk.features_update.update_passport.update_passport_di

import UpdatePassportApi
import UpdatePassportApproveUseCase
import UpdatePassportRemoteDataSource
import UpdatePassportRemoteDataSourceImpl
import UpdatePassportRepository
import UpdatePassportRepositoryImplementation
import UpdatePassportUploadImageUseCase
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_update.update_passport.update_passport_domain.usecases.UpdatePassportIsTranslationStepEnabledUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val updatePassportModule = module {
    single {
        UpdatePassportUploadImageUseCase(get())
    }
    single {
        UpdatePassportApproveUseCase(get())
    }
    single {
        UpdatePassportIsTranslationStepEnabledUseCase(get())
    }
    single<UpdatePassportRemoteDataSource> {
        UpdatePassportRemoteDataSourceImpl(get(), get())
    }
    single<UpdatePassportRepository> {
        UpdatePassportRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(UpdatePassportApi::class.java)
    }

}