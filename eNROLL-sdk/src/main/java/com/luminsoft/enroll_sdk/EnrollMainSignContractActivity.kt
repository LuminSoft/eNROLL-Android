package com.luminsoft.enroll_sdk

import SignContractViewModel
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
import com.luminsoft.enroll_sdk.features.device_data.device_data_di.deviceDataModule
import com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_di.electronicSignatureModule
import com.luminsoft.enroll_sdk.features.email.email_di.emailModule
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_di.faceCaptureModule
import com.luminsoft.enroll_sdk.features.location.location_di.locationModule
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_di.nationalIdConfirmationModule
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_di.phoneNumbersModule
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_di.securityQuestionsModule
import com.luminsoft.enroll_sdk.features.setting_password.password_di.passwordModule
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_di.termsConditionsModule
import com.luminsoft.enroll_sdk.features_forget.forget_password.forget_password_di.forgetPasswordModule
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_di.lowRiskFRAModule
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_navigation.lowRiskFRARouter
import com.luminsoft.enroll_sdk.features_update.email_update.email_di_update.emailUpdateModule
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_di_update.phoneUpdateModule
import com.luminsoft.enroll_sdk.features_update.security_questions_update.update_security_questions_di.updateSecurityQuestionsModule
import com.luminsoft.enroll_sdk.features_update.update_device_id.update_device_id_di.updateDeviceIdModule
import com.luminsoft.enroll_sdk.features_update.update_location.update_location_di.updateLocationModule
import com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_confirmation_di.updateNationalIdConfirmationModule
import com.luminsoft.enroll_sdk.features_update.update_passport.update_passport_di.updatePassportModule
import com.luminsoft.enroll_sdk.features_update.update_password.update_password_di.updatePasswordModule
import com.luminsoft.enroll_sdk.main.main_di.mainModule
import com.luminsoft.enroll_sdk.main.main_navigation.splashScreenOnBoardingContent
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_di.mainForgetModule
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_di.mainSignContractModule
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_navigation.mainSignContractRouter
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_navigation.splashScreenSignContractContent
import com.luminsoft.enroll_sdk.main_update.main_update_di.mainUpdateModule
import com.luminsoft.enroll_sdk.ui_components.theme.EKYCsDKTheme
import forgetLocationModule
import lostDeviceIdModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import java.util.Locale


@Suppress("DEPRECATION")
class EnrollMainSignContractActivity : ComponentActivity() {

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
            val signContractViewModel: SignContractViewModel =
                koinViewModel<SignContractViewModel>()
            val navController = rememberNavController()

            EKYCsDKTheme(
                appColors = EnrollSDK.appColors,
                dynamicColor = false
            ) {
                NavHost(
                    navController = navController,
                    startDestination = getStartingRoute()
                ) {
                    mainSignContractRouter(navController = navController, signContractViewModel)
                    lowRiskFRARouter(navController = navController, signContractViewModel)
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
                modules(lowRiskFRAModule)
                modules(forgetPasswordModule)
                modules(lostDeviceIdModule)
                modules(termsConditionsModule)
                modules(mainUpdateModule)
                modules(mainSignContractModule)
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
                modules(updateLocationModule)
                modules(emailUpdateModule)
                modules(phoneUpdateModule)
                modules(updateNationalIdConfirmationModule)
                modules(updateDeviceIdModule)
                modules(updateSecurityQuestionsModule)
                modules(updatePasswordModule)
                modules(updatePassportModule)
            }.koin
        }
    }

    private fun getStartingRoute(): String {
        return when (EnrollSDK.enrollMode) {
            EnrollMode.SIGN_CONTRACT -> {
                splashScreenSignContractContent
            }

            else -> {
                return splashScreenOnBoardingContent
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