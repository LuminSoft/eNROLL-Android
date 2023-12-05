package com.luminsoft.ekyc_android_sdk.main.main_onboarding.view_model

import android.content.Context
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.sdk.EkycSdk
import com.luminsoft.ekyc_android_sdk.core.utils.ResourceProvider
import com.luminsoft.ekyc_android_sdk.core.utils.ui
import com.luminsoft.ekyc_android_sdk.main.main_domain.usecases.GenerateOnboardingSessionTokenUsecase
import com.luminsoft.ekyc_android_sdk.main.main_domain.usecases.GenerateOnboardingSessionTokenUsecaseParams
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.ext.koin.androidContext
import java.util.UUID

class OnBoardingViewModel(private val generateOnboardingSessionToken: GenerateOnboardingSessionTokenUsecase) : ViewModel() ,
    MainViewModel
{
    override var loading: MutableStateFlow<Boolean> =MutableStateFlow(false)
    override var isButtonLoading: MutableStateFlow<Boolean> =  MutableStateFlow(false)
    override var failure: MutableStateFlow<SdkFailure?> =  MutableStateFlow(null)
    override var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    override suspend fun retry(navController: NavController) {
        TODO("Not yet implemented")
    }

    var token = MutableStateFlow<String?>(null)
//    private var payResponse = MutableStateFlow<PayResponse?>(null)

    init {
        generateToken()
    }

    private fun generateToken() {

        loading.value = true
        ui {
            val udid:String = UUID.randomUUID().toString()
            println(udid)
            params.value = GenerateOnboardingSessionTokenUsecaseParams(EkycSdk.tenantId,EkycSdk.tenantSecret , udid)

            val response: Either<SdkFailure, String> = generateOnboardingSessionToken.call(params.value as GenerateOnboardingSessionTokenUsecaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let { it1 ->
                        token.value = it1
                        loading.value = false
                    }
                })
        }


    }
}