package com.luminsoft.enroll_sdk

import com.luminsoft.enroll_sdk.ui_components.theme.EKYCsDKTheme
import android.annotation.SuppressLint
import android.content.res.Configuration
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
import com.luminsoft.enroll_sdk.main.main_navigation.splashScreenOnBoardingContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_navigation.splashScreenAuthContent
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_di.mainForgetModule
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_navigation.mainForgetRouter
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_navigation.splashScreenForgetContent
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.main_forget.view_model.ForgetViewModel
import com.luminsoft.enroll_sdk.main_update.main_update_navigation.splashScreenUpdateContent
import forgetLocationModule
import forgetLocationRouter
import forgetPasswordModule
import forgetPasswordRouter
import lostDeviceIdModule
import lostDeviceIdRouter
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import java.util.Locale


@Suppress("DEPRECATION")
class EnrollMainForgetActivity : ComponentActivity() {

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
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
        setLocale()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        setContent {
            val forgetViewModel: ForgetViewModel = koinViewModel<ForgetViewModel>()
            val navController = rememberNavController()

            EKYCsDKTheme(
                appColors = EnrollSDK.appColors
            ) {
                NavHost(
                    navController = navController,
                    startDestination = getStartingRoute()
                ) {
                    mainForgetRouter(navController = navController, forgetViewModel)
                    forgetLocationRouter(navController = navController, forgetViewModel)
                    forgetPasswordRouter(navController = navController, forgetViewModel)
                    lostDeviceIdRouter(navController = navController)


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
                modules(mainForgetModule)
                modules(forgetLocationModule)
                modules(forgetPasswordModule)
                modules(lostDeviceIdModule)
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

            EnrollMode.FORGET_PROFILE_DATA -> {
                splashScreenForgetContent
            }

        }
    }

    @Suppress("DEPRECATION")
    private fun setLocale() {
        val locale = EnrollSDK.localizationCode.name.let { Locale(it) }
        Locale.setDefault(locale)

        val config: Configuration = baseContext.resources.configuration
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
    }
}