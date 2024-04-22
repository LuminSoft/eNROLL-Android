package com.luminsoft.enroll_sdk.features.email.email_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.email.email_data.email_api.EmailApi
import com.luminsoft.enroll_sdk.features.email.email_data.email_remote_data_source.EmailRemoteDataSource
import com.luminsoft.enroll_sdk.features.email.email_data.email_remote_data_source.EmailRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features.email.email_data.email_repository.EmailRepositoryImplementation
import com.luminsoft.enroll_sdk.features.email.email_domain.repository.EmailRepository
import org.koin.dsl.module

val emailModule = module{

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