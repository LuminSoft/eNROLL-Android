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
import checkDeviceIdAuthUpdateRouter
import com.luminsoft.enroll_sdk.core.models.EnrollMode
import com.luminsoft.enroll_sdk.core.models.sdkModule
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.core.utils.WifiService
import com.luminsoft.enroll_sdk.main.main_navigation.splashScreenOnBoardingContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_navigation.splashScreenAuthContent
import com.luminsoft.enroll_sdk.main_update.main_update_di.mainUpdateModule
import com.luminsoft.enroll_sdk.main_update.main_update_navigation.mainUpdateRouter
import com.luminsoft.enroll_sdk.main_update.main_update_navigation.splashScreenUpdateContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.theme.AppColors
import deviceIdAuthUpdateModule
import faceCaptureAuthUpdateModule
import faceCaptureAuthUpdateRouter
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import securityQuestionAuthUpdateModule
import securityQuestionAuthUpdateRouter
import updateLocationModule
import updateLocationRouter
import updateNationalIdConfirmationModule
import updateNationalIdRouter


@Suppress("DEPRECATION")
class EnrollMainUpdateActivity : ComponentActivity() {

    override fun onBackPressed() {

    }
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
            val updateViewModel: UpdateViewModel = koinViewModel<UpdateViewModel>()
            val navController = rememberNavController()

            EKYCsDKTheme(appColors = AppColors()) {
                NavHost(
                    navController = navController,
                    startDestination = getStartingRoute()
                ) {
                    mainUpdateRouter(navController = navController, updateViewModel)
                    checkDeviceIdAuthUpdateRouter(navController = navController, updateViewModel)
                    securityQuestionAuthUpdateRouter(navController = navController, updateViewModel)
                    faceCaptureAuthUpdateRouter(navController = navController, updateViewModel)
                    updateLocationRouter(navController = navController, updateViewModel)
                    updateNationalIdRouter(navController = navController, updateViewModel)

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
                modules(mainUpdateModule)
                modules(deviceIdAuthUpdateModule)
                modules(securityQuestionAuthUpdateModule)
                modules(faceCaptureAuthUpdateModule)
                modules(updateLocationModule)
                modules(updateNationalIdConfirmationModule)
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

            EnrollMode.UPDATE -> {
                splashScreenUpdateContent
            }

            else -> {
                return splashScreenUpdateContent
            }
        }
    }
}