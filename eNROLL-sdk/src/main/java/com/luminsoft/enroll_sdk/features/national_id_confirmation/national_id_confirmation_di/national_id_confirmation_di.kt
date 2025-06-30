package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_api.NationalIdConfirmationApi
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_remote_data_source.NationalIdConfirmationRemoteDataSource
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_remote_data_source.NationalIdConfirmationRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_repository.NationalIdConfirmationRepositoryImplementation
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.repository.NationalIdConfirmationRepository
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationApproveUseCase
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationUploadImageUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val nationalIdConfirmationModule = module {
    single {
        PersonalConfirmationUploadImageUseCase(get())
    }
    single {
        PersonalConfirmationApproveUseCase(get())
    }
    single<NationalIdConfirmationRemoteDataSource> {
        NationalIdConfirmationRemoteDataSourceImpl(get(), get())
    }
    single<NationalIdConfirmationRepository> {
        NationalIdConfirmationRepositoryImplementation(get())
    }
    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(NationalIdConfirmationApi::class.java)
    }
}