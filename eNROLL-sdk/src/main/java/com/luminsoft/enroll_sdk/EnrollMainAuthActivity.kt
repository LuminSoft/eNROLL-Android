package com.luminsoft.enroll_sdk

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.luminsoft.enroll_sdk.core.models.EnrollMode
import com.luminsoft.enroll_sdk.core.models.sdkModule
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.core.utils.WifiService
import com.luminsoft.enroll_sdk.features.device_data.device_data_di.deviceDataModule
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
import com.luminsoft.enroll_sdk.ui_components.theme.EKYCsDKTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin


@Suppress("DEPRECATION")
class EnrollMainAuthActivity : ComponentActivity() {
    //    val authViewModel: OnBoardingViewModel by viewModel()
//    val authViewModel: AuthViewModel by viewModel()

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

            EKYCsDKTheme(dynamicColor = false) {
                NavHost(
                    navController = navController,
                    startDestination = getStartingRoute()
                ) {
                    mainAuthRouter(navController = navController, authViewModel)
                    passwordAuthRouter(navController = navController, authViewModel)
                    phoneAuthRouter(navController = navController, authViewModel)
                    mailAuthRouter(navController = navController, authViewModel)
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
                modules(mainAuthModule)
                modules(deviceDataModule)
                modules(passwordAuthModule)
                modules(mailAuthModule)
                modules(phoneAuthModule)
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