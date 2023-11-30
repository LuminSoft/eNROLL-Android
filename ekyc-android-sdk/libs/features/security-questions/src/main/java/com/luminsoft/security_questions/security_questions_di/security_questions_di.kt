package com.luminsoft.security_questions.security_questions_di

import com.luminsoft.core.network.AuthInterceptor
import com.luminsoft.core.network.RetroClient
import com.luminsoft.security_questions.security_questions_domain.usecases.GetSavedCardsUseCase
import com.luminsoft.security_questions.security_questions_data.security_questions_api.SecurityQuestionsApi
import com.luminsoft.security_questions.security_questions_data.security_questions_remote_data_source.SecurityQuestionsRemoteDataSource
import com.luminsoft.security_questions.security_questions_data.security_questions_remote_data_source.SecurityQuestionsRemoteDataSourceImpl
import com.luminsoft.security_questions.security_questions_data.security_questions_repository.SecurityQuestionsRepositoryImplementation
import com.luminsoft.security_questions.security_questions_domain.repository.SecurityQuestionsRepository
import com.luminsoft.security_questions.security_questions_presentation.security_questions_onboarding.view_model.SecurityQuestionsOnBoardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val securityQuestionsModule = module{
    single {
        GetSavedCardsUseCase(get())
    }
    single<SecurityQuestionsRemoteDataSource> {
        SecurityQuestionsRemoteDataSourceImpl(get(),get())
    }
    single<SecurityQuestionsRepository> {
        SecurityQuestionsRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(SecurityQuestionsApi::class.java)
    }
    viewModel{
        SecurityQuestionsOnBoardingViewModel(get())
    }

}