package com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model

import android.graphics.Bitmap
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.network.RetroClient
import com.luminsoft.ekyc_android_sdk.core.sdk.EkycSdk
import com.luminsoft.ekyc_android_sdk.core.utils.ui
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingPrescanScreen
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.get_onboaring_configurations.StepModel
import com.luminsoft.ekyc_android_sdk.main.main_domain.usecases.GenerateOnboardingSessionTokenUsecase
import com.luminsoft.ekyc_android_sdk.main.main_domain.usecases.GenerateOnboardingSessionTokenUsecaseParams
import com.luminsoft.ekyc_android_sdk.main.main_domain.usecases.GetOnboardingStepConfigurationsUsecase
import com.luminsoft.ekyc_android_sdk.main.main_domain.usecases.GetOnboardingStepConfigurationsUsecaseParams
import com.luminsoft.ekyc_android_sdk.main.main_domain.usecases.InitializeRequestUsecase
import com.luminsoft.ekyc_android_sdk.main.main_domain.usecases.InitializeRequestUsecaseParams
import com.luminsoft.ekyc_android_sdk.main.main_navigation.onBoardingScreenContent
import com.luminsoft.ekyc_android_sdk.main.main_navigation.passwordScreenContent
import com.luminsoft.ekyc_android_sdk.main.main_presentation.common.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class OnBoardingViewModel(
    private val generateOnboardingSessionToken: GenerateOnboardingSessionTokenUsecase,
    private val getOnboardingStepConfigurationsUsecase: GetOnboardingStepConfigurationsUsecase,
    private val initializeRequestUsecase: InitializeRequestUsecase
) : ViewModel(),
    MainViewModel {
    override var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    override var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    override var token: MutableStateFlow<String?> = MutableStateFlow(null)
    var steps: MutableStateFlow<List<StepModel>?> = MutableStateFlow(null)
    var navController: NavController? = null
    var faceImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    var nationalIdFrontImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    override fun retry(navController: NavController) {
        TODO("Not yet implemented")
    }

    fun initRequest(navController: NavController) {
        loading.value = true
        ui {
            val udid: String = UUID.randomUUID().toString()
            val manufacturer: String = Build.MANUFACTURER
            val deviceModel: String = Build.MODEL

            params.value = InitializeRequestUsecaseParams(
                udid,
                manufacturer,
                deviceModel
            )
            val response: Either<SdkFailure, Null> =
                initializeRequestUsecase.call(params.value as InitializeRequestUsecaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    loading.value = false
                    navController.navigate(nationalIdOnBoardingPrescanScreen)
                })

        }
    }

    init {
        generateToken()
    }

//    private var payResponse = MutableStateFlow<PayResponse?>(null)


    private fun generateToken() {
        loading.value = true
        ui {
//            delay(2000)
            val udid: String = UUID.randomUUID().toString()
            params.value = GenerateOnboardingSessionTokenUsecaseParams(
                EkycSdk.tenantId,
                EkycSdk.tenantSecret,
                udid
            )

            val response: Either<SdkFailure, String> =
                generateOnboardingSessionToken.call(params.value as GenerateOnboardingSessionTokenUsecaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let { it1 ->
                        token.value = it1
                        RetroClient.setToken(it1)
                        params.value = GetOnboardingStepConfigurationsUsecaseParams()
                        val response: Either<SdkFailure, List<StepModel>> =
                            getOnboardingStepConfigurationsUsecase.call(params.value as GetOnboardingStepConfigurationsUsecaseParams)
                        response.fold({
                            failure.value = it
                            loading.value = false
                        }, { list ->
                            steps.value = list
                            loading.value = false
//                            navController!!.navigate(onBoardingScreenContent)
                            navController!!.navigate(passwordScreenContent)
                        })

                    }
                })
        }


    }
}