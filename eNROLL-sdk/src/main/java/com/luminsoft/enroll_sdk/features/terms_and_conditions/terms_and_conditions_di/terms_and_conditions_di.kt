package com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_di

import GetTermsIdUseCase
import GetTermsPdfFileByIdUseCase
import TermsConditionsApi
import TermsConditionsOnBoardingViewModel
import TermsConditionsRemoteDataSource
import TermsConditionsRemoteDataSourceImpl
import TermsConditionsRepository
import TermsConditionsRepositoryImplementation
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_domain.usecases.AcceptTermsUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val termsConditionsModule = module {
    single {
        GetTermsIdUseCase(get())
    }
    single {
        GetTermsPdfFileByIdUseCase(get())
    }

    single {
        AcceptTermsUseCase(get())
    }


    single<TermsConditionsRemoteDataSource> {
        TermsConditionsRemoteDataSourceImpl(get(), get())
    }



    single<TermsConditionsRepository> {
        TermsConditionsRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(TermsConditionsApi::class.java)
    }


    viewModel {
        TermsConditionsOnBoardingViewModel(get(), get(), get(), get())
    }

}