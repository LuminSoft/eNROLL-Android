package com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_confirmation_di

import IsTranslationStepEnabledUseCase
import UpdateNationalIdConfirmationRemoteDataSource
import UpdateNationalIdConfirmationRemoteDataSourceImpl
import UpdateNationalIdConfirmationRepository
import UpdateNationalIdConfirmationRepositoryImplementation
import UpdatePersonalConfirmationApproveUseCase
import UpdatePersonalConfirmationUploadImageUseCase
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_confirmation_data.update_national_id_confirmation_api.UpdateNationalIdConfirmationApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val updateNationalIdConfirmationModule = module {
    single {
        UpdatePersonalConfirmationUploadImageUseCase(get())
    }
    single {
        UpdatePersonalConfirmationApproveUseCase(get())
    }
    single {
        IsTranslationStepEnabledUseCase(get())
    }
    single<UpdateNationalIdConfirmationRemoteDataSource> {
        UpdateNationalIdConfirmationRemoteDataSourceImpl(get(), get())
    }
    single<UpdateNationalIdConfirmationRepository> {
        UpdateNationalIdConfirmationRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient)
            .create(UpdateNationalIdConfirmationApi::class.java)
    }


}