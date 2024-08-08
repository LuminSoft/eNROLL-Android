package com.luminsoft.enroll_sdk

import EKYCsDKTheme
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import checkIMEIAuthModule
import checkIMEIAuthRouter
import com.luminsoft.enroll_sdk.core.models.EnrollMode
import com.luminsoft.enroll_sdk.core.models.sdkModule
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.core.utils.WifiService
import com.luminsoft.enroll_sdk.features.device_data.device_data_di.deviceDataModule
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_di.checkExpiryDateAuthModule
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_navigation.checkExpiryDateAuthRouter
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_di.faceCaptureAuthModule
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_navigation.faceCaptureAuthRouter
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_di.locationAuthModule
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_navigation.locationAuthRouter
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_di.mailAuthModule
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_navigation.mailAuthRouter
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_di.passwordAuthModule
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_navigation.passwordAuthRouter
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_di.phoneAuthModule
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_navigation.phoneAuthRouter
import com.luminsoft.enroll_sdk.main.main_navigation.splashScreenOnBoardingContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_di.mainAuthModule
import com.luminsoft.enroll_sdk.main_auth.main_auth_navigation.mainAuthRouter
import com.luminsoft.enroll_sdk.main_auth.main_auth_navigation.splashScreenAuthContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import securityQuestionAuthModule
import securityQuestionAuthRouter


@Suppress("DEPRECATION")
class EnrollMainAuthActivity : ComponentActivity() {

    private fun setupServices() {
        WifiService.instance.initializeWithApplicationContext(this)
        ResourceProvider.instance.initializeWithApplicationContext(this)
        RetroClient.setBaseUrl(EnrollSDK.getApisUrl())
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        getKoin(this)
        setupServices()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        setContent {
            val authViewModel: AuthViewModel = koinViewModel<AuthViewModel>()
            val navController = rememberNavController()

            EKYCsDKTheme(dynamicColor = false, appColors = EnrollSDK.appColors) {
                NavHost(
                    navController = navController,
                    startDestination = getStartingRoute()
                ) {
                    mainAuthRouter(navController = navController, authViewModel)
                    passwordAuthRouter(navController = navController, authViewModel)
                    phoneAuthRouter(navController = navController, authViewModel)
                    mailAuthRouter(navController = navController, authViewModel)
                    locationAuthRouter(navController = navController, authViewModel)
                    securityQuestionAuthRouter(navController = navController, authViewModel)
                    faceCaptureAuthRouter(navController = navController, authViewModel)
                    checkIMEIAuthRouter(navController = navController, authViewModel)
                    checkExpiryDateAuthRouter(navController = navController, authViewModel)
                }
            }
        }
    }

    private fun getKoin(activity: ComponentActivity): Koin {
        return if (activity is KoinComponent) {
            activity.getKoin()
        } else {
            GlobalContext.getOrNull() ?: startKoin {
                androidContext(activity.applicationContext)
                modules(sdkModule)
                modules(mainAuthModule)
                modules(deviceDataModule)
                modules(passwordAuthModule)
                modules(mailAuthModule)
                modules(phoneAuthModule)
                modules(locationAuthModule)
                modules(checkExpiryDateAuthModule)
                modules(checkIMEIAuthModule)
                modules(faceCaptureAuthModule)
                modules(securityQuestionAuthModule)
            }.koin
        }
    }

    private fun getStartingRoute(): String {
        return when (EnrollSDK.enrollMode) {
            EnrollMode.ONBOARDING -> {
                splashScreenOnBoardingContent
            }

            EnrollMode.AUTH -> {
                splashScreenAuthContent
            }

            else -> {
                return splashScreenOnBoardingContent
            }
        }
    }
}