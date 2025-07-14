package com.luminsoft.enroll_sdk.features.setting_password.password_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_api.PasswordApi
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_remote_data_source.PasswordRemoteDataSource
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_remote_data_source.PasswordRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features.setting_password.password_data.password_repository.PasswordRepositoryImplementation
import com.luminsoft.enroll_sdk.features.setting_password.password_domain.repository.PasswordRepository
import com.luminsoft.enroll_sdk.features.setting_password.password_domain.usecases.OnboardingSettingPasswordUseCase
import com.luminsoft.enroll_sdk.features.setting_password.password_onboarding.view_model.PasswordOnBoardingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val passwordModule = module {
    single {
        OnboardingSettingPasswordUseCase(get())
    }
    single<PasswordRemoteDataSource> {
        PasswordRemoteDataSourceImpl(get(), get())
    }
    single<PasswordRepository> {
        PasswordRepositoryImplementation(get())
    }
    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(PasswordApi::class.java)
    }
    viewModel {
        PasswordOnBoardingViewModel(get())
    }


}