package com.luminsoft.enroll_sdk.features_update.security_questions_update.update_security_questions_di

import GetSecurityQuestionsUpdateUseCase
import UpdateSecurityQuestionsApi
import UpdateSecurityQuestionsRemoteDataSource
import UpdateSecurityQuestionsRemoteDataSourceImpl
import UpdateSecurityQuestionsRepository
import UpdateSecurityQuestionsRepositoryImplementation
import UpdateSecurityQuestionsUseCase
import UpdateSecurityQuestionsViewModel
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val updateSecurityQuestionsModule = module {
    single {
        GetSecurityQuestionsUpdateUseCase(get())
    }
    single {
        UpdateSecurityQuestionsUseCase(get())
    }
    single<UpdateSecurityQuestionsRemoteDataSource> {
        UpdateSecurityQuestionsRemoteDataSourceImpl(get(), get())
    }
    single<UpdateSecurityQuestionsRepository> {
        UpdateSecurityQuestionsRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(UpdateSecurityQuestionsApi::class.java)
    }
    viewModel {
        UpdateSecurityQuestionsViewModel(get(), get(), get())
    }

}