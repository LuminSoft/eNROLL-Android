package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_di

import com.luminsoft.enroll_sdk.core.network.AuthInterceptor
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra.view_model.CurrentContractLowRiskFRAViewModel
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.low_risk_fra_api.LowRiskFRAApi
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.phone_low_risk_fra_remote_data_source.LowRiskFRARemoteDataSource
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.phone_low_risk_fra_remote_data_source.LowRiskFRARemoteDataSourceImpl
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_data.phone_low_risk_fra_repository.LowRiskFRARepositoryImplementation
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.repository.LowRiskFRARepository
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases.LowRiskFRASendOTPUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val lowRiskFRAModule = module {
    single {
        LowRiskFRASendOTPUseCase(get())
    }
    single<LowRiskFRARemoteDataSource> {
        LowRiskFRARemoteDataSourceImpl(get(), get())
    }
    single<LowRiskFRARepository> {
        LowRiskFRARepositoryImplementation(get())
    }

    single {
        val context = androidContext()
        val okHttpClient = RetroClient.provideOkHttpClient(AuthInterceptor(), context)
        RetroClient.provideRetrofit(okHttpClient).create(LowRiskFRAApi::class.java)
    }
    viewModel {
        CurrentContractLowRiskFRAViewModel(get(), get(), get(), get(), get(), get())
    }


}