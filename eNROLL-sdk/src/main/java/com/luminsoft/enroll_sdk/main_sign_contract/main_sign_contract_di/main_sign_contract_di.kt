package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_di

import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_api.MainSignContractApi
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_repository.MainSignContractRepositoryImplementation
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_presentation.main_sign_contract.view_model.SignContractViewModel
import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_remote_data_source.MainSignContractRemoteDataSource
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_remote_data_source.MainSignContractRemoteDataSourceImpl
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.repository.MainSignContractRepository
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.GenerateSignContractSessionTokenUsecase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.GetSignContractStepsUsecase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.usecases.InitializeRequestSignContractUsecase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainSignContractModule = module {
    single {
        GenerateSignContractSessionTokenUsecase(get())
    }
    single {
        InitializeRequestSignContractUsecase(get())
    }
    single {
        GetSignContractStepsUsecase(get())
    }
    single<MainSignContractRemoteDataSource> {
        MainSignContractRemoteDataSourceImpl(get(), get())
    }
    single<MainSignContractRepository> {
        MainSignContractRepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(MainSignContractApi::class.java)
    }
    viewModel {
        SignContractViewModel(
            get(), get(), get(), context = androidApplication()
        )
    }


}