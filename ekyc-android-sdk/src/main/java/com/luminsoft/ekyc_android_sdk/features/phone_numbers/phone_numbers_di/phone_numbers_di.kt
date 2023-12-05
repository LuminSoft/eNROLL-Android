package com.luminsoft.ekyc_android_sdk.features.phone_numbers.phone_numbers_di
import com.luminsoft.ekyc_android_sdk.core.network.AuthInterceptor
import com.luminsoft.ekyc_android_sdk.core.network.RetroClient
import com.luminsoft.ekyc_android_sdk.features.phone_numbers.phone_numbers_domain.usecases.GetSavedCardsUseCase
import com.luminsoft.ekyc_android_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_api.PhoneNumbersApi
import com.luminsoft.ekyc_android_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_remote_data_source.PhoneNumbersRemoteDataSource
import com.luminsoft.ekyc_android_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_remote_data_source.PhoneNumbersRemoteDataSourceImpl
import com.luminsoft.ekyc_android_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_repository.PhoneNumbersRepositoryImplementation
import com.luminsoft.ekyc_android_sdk.features.phone_numbers.phone_numbers_domain.repository.PhoneNumbersRepository
import org.koin.dsl.module

val phoneNumbersModule = module{
    single {
        GetSavedCardsUseCase(get())
    }
    single<PhoneNumbersRemoteDataSource> {
        PhoneNumbersRemoteDataSourceImpl(get(),get())
    }
    single<PhoneNumbersRepository> {
        PhoneNumbersRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(PhoneNumbersApi::class.java)
    }

}