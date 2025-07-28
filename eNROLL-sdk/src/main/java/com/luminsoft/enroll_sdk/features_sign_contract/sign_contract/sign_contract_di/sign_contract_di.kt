package com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract.view_model.SignContractOTPViewModel
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_api.SignContractApi
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_remote_data_source.SignContractRemoteDataSource
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_remote_data_source.SignContractRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_repository.SignContractRepositoryImplementation
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_domain.repository.SignContractRepository
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_domain.usecases.SignContractSendOTPUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val signContractModule = module {
    single {
        SignContractSendOTPUseCase(get())
    }
    single<SignContractRemoteDataSource> {
        SignContractRemoteDataSourceImpl(get(), get())
    }
    single<SignContractRepository> {
        SignContractRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(SignContractApi::class.java)
    }
    viewModel {
        SignContractOTPViewModel(get(), get())
    }


}