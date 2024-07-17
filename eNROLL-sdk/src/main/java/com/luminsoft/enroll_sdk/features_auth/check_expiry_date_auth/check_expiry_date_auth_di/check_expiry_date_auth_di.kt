package com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_data.check_expiry_date_auth_api.CheckExpiryDateAuthApi
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_data.check_expiry_date_auth_remote_data_source.CheckExpiryDateAuthRemoteDataSource
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_data.check_expiry_date_auth_remote_data_source.CheckExpiryDateAuthRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_data.check_expiry_date_auth_repository.CheckExpiryDateAuthRepositoryImplementation
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_domain.repository.CheckExpiryDateAuthRepository
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_domain.usecases.AuthCheckExpiryDateUseCase
import org.koin.dsl.module

val checkExpiryDateAuthModule = module {
    single {
        AuthCheckExpiryDateUseCase(get())
    }
    single<CheckExpiryDateAuthRemoteDataSource> {
        CheckExpiryDateAuthRemoteDataSourceImpl(get(), get())
    }
    single<CheckExpiryDateAuthRepository> {
        CheckExpiryDateAuthRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(CheckExpiryDateAuthApi::class.java)
    }
}