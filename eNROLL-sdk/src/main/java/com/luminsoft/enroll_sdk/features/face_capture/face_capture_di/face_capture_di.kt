package com.luminsoft.enroll_sdk.features.face_capture.face_capture_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_api.FaceCaptureApi
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_remote_data_source.FaceCaptureRemoteDataSource
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_remote_data_source.FaceCaptureRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_repository.FaceCaptureRepositoryImplementation
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_domain.repository.FaceCaptureRepository
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_domain.usecases.FaceCaptureUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val faceCaptureModule = module {
    single {
        FaceCaptureUseCase(get())
    }
    single<FaceCaptureRemoteDataSource> {
        FaceCaptureRemoteDataSourceImpl(get(), get())
    }
    single<FaceCaptureRepository> {
        FaceCaptureRepositoryImplementation(get())
    }
    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(FaceCaptureApi::class.java)
    }

}