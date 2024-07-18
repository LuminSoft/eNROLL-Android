package com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_data.face_capture_auth_api.FaceCaptureAuthApi
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_data.face_capture_auth_remote_data_source.FaceCaptureAuthRemoteDataSource
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_data.face_capture_auth_remote_data_source.FaceCaptureAuthRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_data.face_capture_auth_repository.FaceCaptureAuthRepositoryImplementation
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_domain.repository.FaceCaptureAuthRepository
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_domain.usecases.FaceCaptureAuthUseCase
import org.koin.dsl.module

val faceCaptureAuthModule = module {
    single {
        FaceCaptureAuthUseCase(get())
    }
    single<FaceCaptureAuthRemoteDataSource> {
        FaceCaptureAuthRemoteDataSourceImpl(get(), get())
    }
    single<FaceCaptureAuthRepository> {
        FaceCaptureAuthRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(FaceCaptureAuthApi::class.java)
    }

}