package com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_data.location_auth_api.LocationAuthApi
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_data.location_auth_remote_data_source.LocationAuthRemoteDataSource
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_data.location_auth_remote_data_source.LocationAuthRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_data.location_auth_repository.LocationAuthRepositoryImplementation
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_domain.repository.LocationAuthRepository
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_domain.usecases.PostLocationAuthUseCase
import org.koin.dsl.module

val locationAuthModule = module {
    single {
        PostLocationAuthUseCase(get())
    }
    single<LocationAuthRemoteDataSource> {
        LocationAuthRemoteDataSourceImpl(get(), get())
    }
    single<LocationAuthRepository> {
        LocationAuthRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(LocationAuthApi::class.java)
    }

}