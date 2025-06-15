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
import com.luminsoft.enroll_sdk.features.check_aml.check_aml_di.checkAmlModule
import com.luminsoft.enroll_sdk.features.check_aml.check_aml_navigation.checkAmlRouter
import com.luminsoft.enroll_sdk.features.device_data.device_data_di.deviceDataModule
import com.luminsoft.enroll_sdk.features.device_data.device_data_navigation.deviceDataRouter
import com.luminsoft.enroll_sdk.features.email.email_di.emailModule
import com.luminsoft.enroll_sdk.features.email.email_navigation.emailRouter
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_di.faceCaptureModule
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_navigation.faceCaptureRouter
import com.luminsoft.enroll_sdk.features.location.location_di.locationModule
import com.luminsoft.enroll_sdk.features.location.location_navigation.locationRouter
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_di.nationalIdConfirmationModule
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdRouter
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_di.phoneNumbersModule
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_navigation.phoneNumberRouter
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_di.securityQuestionsModule
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_navigation.securityQuestionsRouter
import com.luminsoft.enroll_sdk.features.setting_password.password_di.passwordModule
import com.luminsoft.enroll_sdk.features.setting_password.password_navigation.settingPasswordRouter
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_di.checkExpiryDateAuthModule
import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_di.checkIMEIAuthModule
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_di.faceCaptureAuthModule
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_di.locationAuthModule
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_di.mailAuthModule
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_di.passwordAuthModule
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_di.phoneAuthModule
import com.luminsoft.enroll_sdk.features_auth_update.device_id_auth_update.device_id_auth_update_di.deviceIdAuthUpdateModule
import com.luminsoft.enroll_sdk.features_update.email_update.email_di_update.emailUpdateModule
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_di_update.phoneUpdateModule
import com.luminsoft.enroll_sdk.main.main_di.mainModule
import com.luminsoft.enroll_sdk.main.main_navigation.mainRouter
import com.luminsoft.enroll_sdk.main.main_navigation.splashScreenOnBoardingContent
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.main_auth.main_auth_di.mainAuthModule
import com.luminsoft.enroll_sdk.main_auth.main_auth_navigation.splashScreenAuthContent
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_di.mainForgetModule
import com.luminsoft.enroll_sdk.main_update.main_update_di.mainUpdateModule
import com.luminsoft.enroll_sdk.ui_components.theme.EKYCsDKTheme
import electronicSignatureModule
import electronicSignatureRouter
import faceCaptureAuthUpdateModule
import forgetLocationModule
import forgetPasswordModule
import lostDeviceIdModule
import mailAuthUpdateModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import passwordAuthUpdateModule
import phoneAuthUpdateModule
import securityQuestionAuthModule
import securityQuestionAuthUpdateModule
import termsConditionsModule
import termsConditionsRouter
import updateDeviceIdModule
import updateLocationModule
import updateNationalIdConfirmationModule
import updatePassportModule
import updatePasswordModule
import updateSecurityQuestionsModule
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

        getKoin(this)
        setupServices()
        setLocale()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

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
                    termsConditionsRouter(navController = navController, onBoardingViewModel)
                    mainRouter(navController = navController, onBoardingViewModel)
                    nationalIdRouter(navController = navController, onBoardingViewModel)
                    deviceDataRouter(navController = navController)
                    emailRouter(navController = navController, onBoardingViewModel)
                    faceCaptureRouter(navController = navController, onBoardingViewModel)
                    locationRouter(
                        navController = navController,
                        onBoardingViewModel
                    )
                    checkAmlRouter(
                        navController = navController,
                        onBoardingViewModel
                    )
                    phoneNumberRouter(
                        navController = navController,
                        onBoardingViewModel = onBoardingViewModel
                    )
                    securityQuestionsRouter(
                        navController = navController,
                        onBoardingViewModel = onBoardingViewModel
                    )
                    settingPasswordRouter(
                        navController = navController,
                        onBoardingViewModel = onBoardingViewModel
                    )
                    electronicSignatureRouter(
                        navController = navController,
                        onBoardingViewModel = onBoardingViewModel
                    )
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
                //TODO: refactor later
                modules(forgetLocationModule)
                modules(forgetPasswordModule)
                modules(lostDeviceIdModule)
                modules(passwordAuthModule)
                modules(mailAuthModule)
                modules(phoneAuthModule)
                modules(locationAuthModule)
                modules(checkExpiryDateAuthModule)
                modules(checkIMEIAuthModule)
                modules(faceCaptureAuthModule)
                modules(securityQuestionAuthModule)
                modules(termsConditionsModule)
                modules(mainUpdateModule)
                modules(mainAuthModule)
                modules(checkAmlModule)
                modules(deviceDataModule)
                modules(emailModule)
                modules(faceCaptureModule)
                modules(locationModule)
                modules(nationalIdConfirmationModule)
                modules(phoneNumbersModule)
                modules(securityQuestionsModule)
                modules(passwordModule)
                modules(electronicSignatureModule)
                modules(sdkModule)
                modules(mainForgetModule)
                modules(mainModule)
                modules(deviceIdAuthUpdateModule)
                modules(securityQuestionAuthUpdateModule)
                modules(faceCaptureAuthUpdateModule)
                modules(updateLocationModule)
                modules(emailUpdateModule)
                modules(phoneUpdateModule)
                modules(updateNationalIdConfirmationModule)
                modules(passwordAuthUpdateModule)
                modules(mailAuthUpdateModule)
                modules(phoneAuthUpdateModule)
                modules(updateDeviceIdModule)
                modules(updateSecurityQuestionsModule)
                modules(updatePasswordModule)
                modules(updatePassportModule)

            }.koin
        }
    }

    private fun getStartingRoute(): String {
        return when (EnrollSDK.enrollMode) {
            EnrollMode.ONBOARDING ->
                splashScreenOnBoardingContent


            EnrollMode.AUTH ->
                splashScreenAuthContent


            else ->
                return splashScreenOnBoardingContent

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
