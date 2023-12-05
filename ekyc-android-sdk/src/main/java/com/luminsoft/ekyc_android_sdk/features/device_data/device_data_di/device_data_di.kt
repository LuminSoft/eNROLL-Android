package com.luminsoft.ekyc_android_sdk.features.device_data.device_data_di

import com.luminsoft.ekyc_android_sdk.core.network.AuthInterceptor
import com.luminsoft.ekyc_android_sdk.core.network.RetroClient
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_domain.usecases.GetSavedCardsUseCase
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_data.device_data_api.DeviceDataApi
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_data.device_data_remote_data_source.DeviceDataRemoteDataSource
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_data.device_data_remote_data_source.DeviceDataRemoteDataSourceImpl
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_data.device_data_repository.DeviceDataRepositoryImplementation
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_domain.repository.DeviceDataRepository
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_onboarding.view_model.DeviceDataOnBoardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val deviceDataModule = module{
    single {
        GetSavedCardsUseCase(get())
    }
    single<DeviceDataRemoteDataSource> {
        DeviceDataRemoteDataSourceImpl(get(),get())
    }
    single<DeviceDataRepository> {
        DeviceDataRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(DeviceDataApi::class.java)
    }

    viewModel{
        DeviceDataOnBoardingViewModel()
    }

}