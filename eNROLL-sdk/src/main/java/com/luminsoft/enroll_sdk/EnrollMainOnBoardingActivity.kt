package com.luminsoft.enroll_sdk

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
import com.luminsoft.enroll_sdk.features.device_data.device_data_di.deviceDataModule
import com.luminsoft.enroll_sdk.features.device_data.device_data_navigation.deviceDataRouter
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_di.nationalIdConfirmationModule
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdRouter
import com.luminsoft.enroll_sdk.main.main_di.mainModule
import com.luminsoft.enroll_sdk.main.main_navigation.mainRouter
import com.luminsoft.enroll_sdk.main.main_navigation.splashScreenOnBoardingContent
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.theme.EKYCsDKTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale


@Suppress("DEPRECATION")
class EnrollMainOnBoardingActivity : ComponentActivity() {
    val onBoardingViewModel: OnBoardingViewModel by viewModel()

    private fun setupServices() {
        WifiService.instance.initializeWithApplicationContext(this)
        ResourceProvider.instance.initializeWithApplicationContext(this)
        RetroClient.setBaseUrl(EnrollSDK.getApisUrl())
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val activity = this
        runBlocking {
            val statusCode = checkApiStatusCode()
            if (statusCode != 200) {
                EnrollSDK.isLuminDomain = true
            }
            //ُُُُُTODO
            getKoin(activity)
            setupServices()
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val extras = intent.extras
        if (extras != null) {
            val lang = extras.getString("localCode", "ar")
            setLocale(lang)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        setContent {
            val onBoardingViewModel: OnBoardingViewModel = koinViewModel<OnBoardingViewModel>()
            val navController = rememberNavController()


            EKYCsDKTheme(
                appColors = EnrollSDK.appColors,
                dynamicColor = false
            ) {

                NavHost(
                    navController = navController,
                    startDestination = getStartingRoute()
                ) {
//                    termsConditionsRouter(navController = navController, onBoardingViewModel)
                    mainRouter(navController = navController, onBoardingViewModel)
                    nationalIdRouter(navController = navController, onBoardingViewModel)
                    deviceDataRouter(navController = navController)
//                    emailRouter(navController = navController, onBoardingViewModel)
//                    faceCaptureRouter(navController = navController, onBoardingViewModel)
//                    locationRouter(
//                        navController = navController,
//                        onBoardingViewModel
//                    )
//                    checkAmlRouter(
//                        navController = navController,
//                        onBoardingViewModel
//                    )
//                    phoneNumberRouter(
//                        navController = navController,
//                        onBoardingViewModel = onBoardingViewModel
//                    )
//                    securityQuestionsRouter(
//                        navController = navController,
//                        onBoardingViewModel = onBoardingViewModel
//                    )
//                    settingPasswordRouter(
//                        navController = navController,
//                        onBoardingViewModel = onBoardingViewModel
//                    )
//                    electronicSignatureRouter(
//                        navController = navController,
//                        onBoardingViewModel = onBoardingViewModel
//                    )
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
//                modules(termsConditionsModule)
                modules(sdkModule)
                modules(mainModule)
//                modules(checkAmlModule)
                modules(deviceDataModule)
//                modules(emailModule)
//                modules(faceCaptureModule)
//                modules(locationModule)
                modules(nationalIdConfirmationModule)
//                modules(phoneNumbersModule)
//                modules(securityQuestionsModule)
//                modules(passwordModule)
//                modules(electronicSignatureModule)
            }.koin
        }
    }

    private fun getStartingRoute(): String {
        return when (EnrollSDK.enrollMode) {
            EnrollMode.ONBOARDING ->
                splashScreenOnBoardingContent

            else ->
                return splashScreenOnBoardingContent

        }
    }

    @Suppress("DEPRECATION")
    private fun setLocale(lang: String?) {
        val locale = lang?.let { Locale(it) }
        if (locale != null) {
            Locale.setDefault(locale)
        }
        val config: Configuration = baseContext.resources.configuration
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
    }

    private suspend fun checkApiStatusCode(): Int? {
        return withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            try {
                // Set up the URL and connection
                val url = URL("${EnrollSDK.getApisUrl()}api/v1/Configuration/GetStepsConfiguration")
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
//                connection.setRequestProperty("Authorization", apiKey)
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Accept", "*/*")
                connection.setRequestProperty("Authorization", "Bearer ${EnrollSDK.token}")

                // Get response code
                connection.responseCode
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } finally {
                connection?.disconnect()
            }
        }
    }

}