package com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth.view_model.MailAuthViewModel
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_data.mail_auth_api.MailAuthApi
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_data.mail_auth_remote_data_source.MailAuthRemoteDataSource
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_data.mail_auth_remote_data_source.MailAuthRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_data.mail_auth_repository.MailAuthRepositoryImplementation
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_domain.repository.MailAuthRepository
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_domain.usecases.MailAuthUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mailAuthModule = module {
    single {
        MailAuthUseCase(get())
    }
    single<MailAuthRemoteDataSource> {
        MailAuthRemoteDataSourceImpl(get(), get())
    }
    single<MailAuthRepository> {
        MailAuthRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(MailAuthApi::class.java)
    }
    viewModel {
        MailAuthViewModel(get())
    }


}