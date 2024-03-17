package com.luminsoft.ekyc_android_sdk

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.luminsoft.ekyc_android_sdk.core.utils.WifiService
import com.luminsoft.ekyc_android_sdk.core.models.EkycMode
import com.luminsoft.ekyc_android_sdk.core.models.sdkModule
import com.luminsoft.ekyc_android_sdk.core.network.RetroClient
import com.luminsoft.ekyc_android_sdk.core.sdk.EkycSdk
import com.luminsoft.ekyc_android_sdk.core.utils.ResourceProvider
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_di.deviceDataModule
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_navigation.deviceDataRouter
import com.luminsoft.ekyc_android_sdk.features.email.email_di.emailModule
import com.luminsoft.ekyc_android_sdk.features.email.email_navigation.emailRouter
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_di.faceCaptureModule
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_navigation.faceCaptureRouter
import com.luminsoft.ekyc_android_sdk.features.location.location_di.locationModule
import com.luminsoft.ekyc_android_sdk.features.location.location_navigation.locationRouter
import com.luminsoft.ekyc_android_sdk.features.location.location_onboarding.view_model.LocationOnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_di.nationalIdConfirmationModule
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_navigation.nationalIdRouter
import com.luminsoft.ekyc_android_sdk.features.phone_numbers.phone_numbers_di.phoneNumbersModule
import com.luminsoft.ekyc_android_sdk.features.phone_numbers.phone_numbers_navigation.phoneNumberRouter
import com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_di.securityQuestionsModule
import com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_navigation.securityQuestionsRouter
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_di.passwordModule
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_navigation.settingPasswordRouter
import com.luminsoft.ekyc_android_sdk.main.main_di.mainModule
import com.luminsoft.ekyc_android_sdk.main.main_navigation.mainRouter
import com.luminsoft.ekyc_android_sdk.main.main_navigation.splashScreenAuthContent
import com.luminsoft.ekyc_android_sdk.main.main_navigation.splashScreenOnBoardingContent
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_auth.view_model.AuthViewModel
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.ui_components.theme.EKYCsDKTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin


@Suppress("DEPRECATION")
class EkycMainActivity : ComponentActivity() {
    val onBoardingViewModel: OnBoardingViewModel by viewModel()
    val authViewModel: AuthViewModel by viewModel()

    private fun setupServices() {
        WifiService.instance.initializeWithApplicationContext(this)
        ResourceProvider.instance.initializeWithApplicationContext(this)
        RetroClient.setBaseUrl(EkycSdk.getApisUrl())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getKoin(this)
        setupServices()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        setContent {
            val onBoardingViewModel: OnBoardingViewModel = koinViewModel<OnBoardingViewModel>()
            val navController = rememberNavController()


            EKYCsDKTheme(dynamicColor = false) {
                NavHost(
                    navController = navController,
                    startDestination = getStartingRoute()
                ) {
                    mainRouter(navController = navController, onBoardingViewModel)
                    nationalIdRouter(navController = navController, onBoardingViewModel)
                    deviceDataRouter(navController = navController)
                    emailRouter(navController = navController)
                    faceCaptureRouter(navController = navController, onBoardingViewModel)
                    locationRouter(
                        navController = navController,
                        onBoardingViewModel
                    )
                    phoneNumberRouter(navController = navController)
                    securityQuestionsRouter(navController = navController)
                    settingPasswordRouter(navController = navController)
                }
            }
        }
    }

    private fun getKoin(activity: ComponentActivity): Koin {
        return if (activity is KoinComponent) {
            activity.getKoin()
        } else {
            GlobalContext.getOrNull() ?: startKoin {
                modules(sdkModule)
                modules(mainModule)
                modules(deviceDataModule)
                modules(emailModule)
                modules(faceCaptureModule)
                modules(locationModule)
                modules(nationalIdConfirmationModule)
                modules(phoneNumbersModule)
                modules(securityQuestionsModule)
                modules(passwordModule)
            }.koin
        }
    }

    private fun getStartingRoute(): String {
        return when (EkycSdk.ekycMode) {
            EkycMode.ONBOARDING -> {
                splashScreenOnBoardingContent
            }

            EkycMode.AUTH -> {
                splashScreenAuthContent
            }

            else -> {
                return splashScreenOnBoardingContent
            }
        }
    }
}