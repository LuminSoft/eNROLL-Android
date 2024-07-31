package com.luminsoft.enroll_sdk.sdk

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import com.luminsoft.enroll_sdk.EnrollMainAuthActivity
import com.luminsoft.enroll_sdk.EnrollMainOnBoardingActivity
import com.luminsoft.enroll_sdk.core.models.EnrollCallback
import com.luminsoft.enroll_sdk.core.models.EnrollEnvironment
import com.luminsoft.enroll_sdk.core.models.EnrollMode
import com.luminsoft.enroll_sdk.core.models.LocalizationCode
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import java.util.Locale

object eNROLL {
    @Throws(Exception::class)
    fun init(
        tenantId: String,
        tenantSecret: String,
        applicantId: String,
        levelOfTrustToken: String,
        enrollMode: EnrollMode,
        environment: EnrollEnvironment = EnrollEnvironment.STAGING,
        localizationCode: LocalizationCode = LocalizationCode.EN,
        enrollCallback: EnrollCallback? = null,
        googleApiKey: String? = "",
        skipTutorial: Boolean = false,
    ) {
        if (tenantId.isEmpty())
            throw Exception("Invalid tenant id")
        if (tenantSecret.isEmpty())
            throw Exception("Invalid tenant secret")
        EnrollSDK.environment = environment
        EnrollSDK.tenantSecret = tenantSecret
        EnrollSDK.tenantId = tenantId
        EnrollSDK.applicantId = applicantId
        EnrollSDK.levelOfTrustToken = levelOfTrustToken
        EnrollSDK.googleApiKey = googleApiKey!!
        EnrollSDK.localizationCode = localizationCode
        EnrollSDK.enrollCallback = enrollCallback
        EnrollSDK.enrollMode = enrollMode
        EnrollSDK.skipTutorial = skipTutorial
    }

    fun launch(
        activity: Activity,
    ) {
        if (EnrollSDK.tenantId.isEmpty())
            throw Exception("Invalid tenant id")
        if (EnrollSDK.tenantSecret.isEmpty())
            throw Exception("Invalid tenant secret")
        setLocale(EnrollSDK.localizationCode, activity)
        if (EnrollSDK.enrollMode == EnrollMode.ONBOARDING)
            activity.startActivity(Intent(activity, EnrollMainOnBoardingActivity::class.java))
        else if (EnrollSDK.enrollMode == EnrollMode.AUTH) {
            if (EnrollSDK.applicantId.isEmpty())
                throw Exception("Invalid application id")
            else if (EnrollSDK.levelOfTrustToken.isEmpty())
                throw Exception("Invalid level of trust token")

            activity.startActivity(Intent(activity, EnrollMainAuthActivity::class.java))
        }
    }

    private fun setLocale(lang: LocalizationCode, activity: Activity) {
        val locale = if (lang != LocalizationCode.AR) {
            Locale("en")
        } else {
            Locale("ar")
        }

        val config: Configuration = activity.baseContext.resources.configuration
        config.setLocale(locale)
        activity.baseContext.resources.updateConfiguration(
            config,
            activity.baseContext.resources.displayMetrics
        )
    }
}