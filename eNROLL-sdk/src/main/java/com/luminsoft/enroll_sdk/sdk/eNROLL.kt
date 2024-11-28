package com.luminsoft.enroll_sdk.sdk

//noinspection UsingMaterialAndMaterial3Libraries
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import com.luminsoft.enroll_sdk.EnrollMainAuthActivity
import com.luminsoft.enroll_sdk.EnrollMainForgetActivity
import com.luminsoft.enroll_sdk.EnrollMainOnBoardingActivity
import com.luminsoft.enroll_sdk.EnrollMainUpdateActivity
import com.luminsoft.enroll_sdk.core.models.EnrollCallback
import com.luminsoft.enroll_sdk.core.models.EnrollEnvironment
import com.luminsoft.enroll_sdk.core.models.EnrollMode
import com.luminsoft.enroll_sdk.core.models.LocalizationCode
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.ui_components.theme.AppColors
import java.util.Locale


object eNROLL {
    @Throws(Exception::class)
    fun init(
//        tenantId: String,
//        tenantSecret: String,
        applicantId: String = "",
        token: String = "",
        levelOfTrustToken: String = "",
        enrollMode: EnrollMode,
        environment: EnrollEnvironment = EnrollEnvironment.STAGING,
        localizationCode: LocalizationCode = LocalizationCode.EN,
        enrollCallback: EnrollCallback? = null,
        googleApiKey: String? = "",
        skipTutorial: Boolean = false,
        appColors: AppColors = AppColors(),
        correlationId: String = "",
        fontResource: Int? = 0
    ) {
//        if (tenantId.isEmpty())
//            throw Exception("Invalid tenant id")
//        if (tenantSecret.isEmpty())
//            throw Exception("Invalid tenant secret")
        if (enrollMode == EnrollMode.AUTH) {
            if (applicantId.isEmpty() || levelOfTrustToken.isEmpty())
                throw Exception("Invalid Applicant Id or Level Of Trust Token")
        }
        EnrollSDK.environment = environment
//        EnrollSDK.tenantSecret = tenantSecret
//        EnrollSDK.tenantId = tenantId
        EnrollSDK.applicantId = applicantId
        EnrollSDK.token = token
        EnrollSDK.levelOfTrustToken = levelOfTrustToken
        EnrollSDK.googleApiKey = googleApiKey!!
        EnrollSDK.localizationCode = localizationCode
        EnrollSDK.enrollCallback = enrollCallback
        EnrollSDK.enrollMode = enrollMode
        EnrollSDK.skipTutorial = skipTutorial
        EnrollSDK.appColors = appColors
        EnrollSDK.correlationId = correlationId
        EnrollSDK.fontResource = fontResource!!


    }

    fun launch(
        activity: Activity,
    ) {

//        if (EnrollSDK.tenantId.isEmpty())
//            throw Exception("Invalid tenant id")
//        if (EnrollSDK.tenantSecret.isEmpty())
//            throw Exception("Invalid tenant secret")
        setLocale(EnrollSDK.localizationCode, activity)
        when (EnrollSDK.enrollMode!!) {
            EnrollMode.ONBOARDING -> {
                activity.startActivity(Intent(activity, EnrollMainOnBoardingActivity::class.java))
            }

            EnrollMode.AUTH -> {
                if (EnrollSDK.applicantId.isEmpty())
                    throw Exception("Invalid application id")
                else if (EnrollSDK.levelOfTrustToken.isEmpty())
                    throw Exception("Invalid level of trust token")

                activity.startActivity(Intent(activity, EnrollMainAuthActivity::class.java))
            }

            EnrollMode.UPDATE -> {
                if (EnrollSDK.applicantId.isEmpty())
                    throw Exception("Invalid application id")
                activity.startActivity(Intent(activity, EnrollMainUpdateActivity::class.java))
            }

            EnrollMode.FORGET_PROFILE_DATA -> {

                activity.startActivity(Intent(activity, EnrollMainForgetActivity::class.java))
            }
        }
    }

//    private fun setLocale(lang: LocalizationCode, activity: Activity) {
//        val locale = if (lang.name.lowercase() != LocalizationCode.AR.name.lowercase()) {
//            Locale("en")
//        } else {
//            Locale("ar")
//        }
//
//        val config: Configuration = activity.baseContext.resources.configuration
//        config.setLocale(locale)
//
//        activity.baseContext.resources.updateConfiguration(
//            config,
//            activity.baseContext.resources.displayMetrics
//        )
//    }

    @Suppress("DEPRECATION")
    private fun setLocale(lang: LocalizationCode, activity: Activity) {
        try {
            val locale = if (lang.name.lowercase() != LocalizationCode.AR.name.lowercase()) {
                Locale("en")
            } else {
                Locale("ar")
            }

            Locale.setDefault(locale)

            val config: Configuration = activity.baseContext.resources.configuration
            config.setLocale(locale)
            activity.baseContext.resources.updateConfiguration(
                config,
                activity.baseContext.resources.displayMetrics
            )
            Log.d("LocalizationLog1", Locale.getAvailableLocales().toString())
            Log.d("LocalizationLog2", locale.toString())
        } catch (e: Exception) {
            Log.d("LocalizationLog3", e.toString())
        }
    }

}