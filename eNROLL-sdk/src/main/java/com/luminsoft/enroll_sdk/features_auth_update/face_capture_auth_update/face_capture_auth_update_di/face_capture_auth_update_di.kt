package com.luminsoft.enroll_sdk.features_auth_update.face_capture_auth_update.face_capture_auth_update_di

import FaceCaptureAuthUpdateRemoteDataSource
import FaceCaptureAuthUpdateRemoteDataSourceImpl
import FaceCaptureAuthUpdateRepository
import FaceCaptureAuthUpdateRepositoryImplementation
import FaceCaptureAuthUpdateUseCase
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_auth_update.face_capture_auth_update.face_capture_auth_update_data.face_capture_auth_update_api.FaceCaptureAuthUpdateApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val faceCaptureAuthUpdateModule = module {
    single {
        FaceCaptureAuthUpdateUseCase(get())
    }
    single<FaceCaptureAuthUpdateRemoteDataSource> {
        FaceCaptureAuthUpdateRemoteDataSourceImpl(get(), get())
    }
    single<FaceCaptureAuthUpdateRepository> {
        FaceCaptureAuthUpdateRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(FaceCaptureAuthUpdateApi::class.java)
    }

}