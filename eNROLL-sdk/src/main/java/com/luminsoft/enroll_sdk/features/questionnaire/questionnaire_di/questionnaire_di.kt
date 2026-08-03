package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_api.QuestionnaireApi
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_remote_data_source.QuestionnaireRemoteDataSource
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_remote_data_source.QuestionnaireRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_repository.QuestionnaireRepositoryImplementation
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.repository.QuestionnaireRepository
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases.GenerateQuestionnaireSessionTokenUseCase
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases.GetQuestionnaireQuestionsUseCase
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases.InitializeQuestionnaireRequestUseCase
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases.SubmitQuestionnaireAnswersUseCase
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_presentation.view_model.QuestionnaireViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val questionnaireModule = module {
    single { GenerateQuestionnaireSessionTokenUseCase(get()) }
    single { InitializeQuestionnaireRequestUseCase(get()) }
    single { GetQuestionnaireQuestionsUseCase(get()) }
    single { SubmitQuestionnaireAnswersUseCase(get()) }
    single<QuestionnaireRemoteDataSource> { QuestionnaireRemoteDataSourceImpl(get(), get()) }
    single<QuestionnaireRepository> { QuestionnaireRepositoryImplementation(get()) }
    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(QuestionnaireApi::class.java)
    }
    viewModel {
        QuestionnaireViewModel(get(), get(), get(), get(), androidContext())
    }
}
