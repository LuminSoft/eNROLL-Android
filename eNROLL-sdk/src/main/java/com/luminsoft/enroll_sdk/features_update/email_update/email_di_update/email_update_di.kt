package com.luminsoft.enroll_sdk.features_update.email_update.email_di_update

import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_repository_update.EmailRepositoryUpdateImplementation
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_api_update.EmailApiUpdate
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_remote_data_source_update.EmailRemoteDataSourceUpdate
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_remote_data_source_update.EmailRemoteDataSourceUpdateImpl
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.repository.EmailRepositoryUpdate
import org.koin.dsl.module

val emailUpdateModule = module {

    single<EmailRemoteDataSourceUpdate> {
        EmailRemoteDataSourceUpdateImpl(get(), get())
    }
    single<EmailRepositoryUpdate> {
        EmailRepositoryUpdateImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(EmailApiUpdate::class.java)
    }
}