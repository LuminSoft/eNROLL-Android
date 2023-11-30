package com.luminsoft.face_capture.face_capture_di
import com.luminsoft.core.network.AuthInterceptor
import com.luminsoft.core.network.RetroClient
import com.luminsoft.face_capture.face_capture_domain.usecases.GetSavedCardsUseCase
import com.luminsoft.face_capture.face_capture_data.face_capture_api.FaceCaptureApi
import com.luminsoft.face_capture.face_capture_data.face_capture_remote_data_source.FaceCaptureRemoteDataSource
import com.luminsoft.face_capture.face_capture_data.face_capture_remote_data_source.FaceCaptureRemoteDataSourceImpl
import com.luminsoft.face_capture.face_capture_data.face_capture_repository.FaceCaptureRepositoryImplementation
import com.luminsoft.face_capture.face_capture_domain.repository.FaceCaptureRepository
import org.koin.dsl.module

val faceCaptureModule = module{
    single {
        GetSavedCardsUseCase(get())
    }
    single<FaceCaptureRemoteDataSource> {
        FaceCaptureRemoteDataSourceImpl(get(),get())
    }
    single<FaceCaptureRepository> {
        FaceCaptureRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(FaceCaptureApi::class.java)
    }

}