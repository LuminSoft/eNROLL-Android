package com.luminsoft.ekyc_android_sdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.luminsoft.core.network.RetroClient
import com.luminsoft.ekyc_android_sdk.sdk.sdkModule
import com.luminsoft.core.utils.ResourceProvider
import com.luminsoft.location.location_di.locationModule
import com.luminsoft.cowpay_sdk.utils.WifiService
import com.luminsoft.device_data.device_data_di.deviceDataModule
import com.luminsoft.ekyc_android_sdk.sdk.EkycSdk
import com.luminsoft.email.email_di.emailModule
import com.luminsoft.face_capture.face_capture_di.faceCaptureModule
import com.luminsoft.main.main_di.mainModule
import com.luminsoft.national_id_confirmation.national_id_confirmation_di.nationalIdConfirmationModule
import com.luminsoft.national_id_confirmation.national_id_navigation.nationalIdOnBoardingPrescanScreen
import com.luminsoft.national_id_confirmation.national_id_navigation.nationalIdRouter
import com.luminsoft.password.password_di.passwordModule
import com.luminsoft.phone_numbers.phone_numbers_di.phoneNumbersModule
import com.luminsoft.security_questions.security_questions_di.securityQuestionsModule
import com.luminsoft.ui_components.theme.EKYCsDKTheme
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin


class EkycMainActivity : ComponentActivity() {
    private fun setupServices() {

        WifiService.instance.initializeWithApplicationContext(this)
        ResourceProvider.instance.initializeWithApplicationContext(this)
        RetroClient.setBaseUrl(EkycSdk.getApisUrl())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getKoin(this)
        setupServices()
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            EKYCsDKTheme (dynamicColor = false){
                NavHost(
                    navController = navController,
                    startDestination = nationalIdOnBoardingPrescanScreen
                ) {
                    nationalIdRouter(navController = navController)
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
                modules(deviceDataModule)
                modules(emailModule)
                modules(faceCaptureModule)
                modules(locationModule)
                modules(nationalIdConfirmationModule)
                modules(phoneNumbersModule)
                modules(securityQuestionsModule)
                modules(passwordModule)
                modules(mainModule)
            }.koin
        }

    }
}