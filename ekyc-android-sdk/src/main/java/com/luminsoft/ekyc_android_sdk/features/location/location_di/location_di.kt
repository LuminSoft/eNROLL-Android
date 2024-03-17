package com.luminsoft.ekyc_android_sdk.features.location.location_di
import com.luminsoft.ekyc_android_sdk.core.network.AuthInterceptor
import com.luminsoft.ekyc_android_sdk.core.network.RetroClient
import com.luminsoft.ekyc_android_sdk.features.location.location_domain.usecases.PostLocationUseCase
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_api.LocationApi
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_remote_data_source.LocationRemoteDataSource
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_remote_data_source.LocationRemoteDataSourceImpl
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_repository.LocationRepositoryImplementation
import com.luminsoft.ekyc_android_sdk.features.location.location_domain.repository.LocationRepository
import org.koin.dsl.module

val locationModule = module{
    single {
        PostLocationUseCase(get())
    }
    single<LocationRemoteDataSource> {
        LocationRemoteDataSourceImpl(get(),get())
    }
    single<LocationRepository> {
        LocationRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(LocationApi::class.java)
    }

}