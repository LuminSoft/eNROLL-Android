package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_di
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.GetCountriesUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_api.PhoneNumbersApi
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_remote_data_source.PhoneNumbersRemoteDataSource
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_remote_data_source.PhoneNumbersRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_repository.PhoneNumbersRepositoryImplementation
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.repository.PhoneNumbersRepository
import org.koin.dsl.module

val phoneNumbersModule = module{
    single {
        GetCountriesUseCase(get())
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