package com.luminsoft.ekyc_android_sdk.features.email.email_di

import com.luminsoft.ekyc_android_sdk.core.network.AuthInterceptor
import com.luminsoft.ekyc_android_sdk.core.network.RetroClient
import com.luminsoft.ekyc_android_sdk.features.email.email_domain.usecases.GetSavedCardsUseCase
import com.luminsoft.ekyc_android_sdk.features.email.email_data.email_api.EmailApi
import com.luminsoft.ekyc_android_sdk.features.email.email_data.email_remote_data_source.EmailRemoteDataSource
import com.luminsoft.ekyc_android_sdk.features.email.email_data.email_remote_data_source.EmailRemoteDataSourceImpl
import com.luminsoft.ekyc_android_sdk.features.email.email_data.email_repository.EmailRepositoryImplementation
import com.luminsoft.ekyc_android_sdk.features.email.email_domain.repository.EmailRepository
import org.koin.dsl.module

val emailModule = module{
    single {
        GetSavedCardsUseCase(get())
    }
    single<EmailRemoteDataSource> {
        EmailRemoteDataSourceImpl(get(),get())
    }
    single<EmailRepository> {
        EmailRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(EmailApi::class.java)
    }

}