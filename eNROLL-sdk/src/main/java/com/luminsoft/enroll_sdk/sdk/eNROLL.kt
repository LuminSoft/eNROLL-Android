package com.luminsoft.enroll_sdk.sdk

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import com.luminsoft.enroll_sdk.EnrollMainActivity
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
        EnrollMode: EnrollMode,
        environment: EnrollEnvironment = EnrollEnvironment.STAGING,
        localizationCode: LocalizationCode = LocalizationCode.EN,
        EnrollCallback: EnrollCallback? = null,
        googleApiKey: String? = ""
    ) {
        if (tenantId.isEmpty())
            throw Exception("Invalid tenant id")
        if (tenantSecret.isEmpty())
            throw Exception("Invalid tenant secret")
        EnrollSDK.environment = environment
        EnrollSDK.tenantSecret = tenantSecret
        EnrollSDK.tenantId = tenantId
        EnrollSDK.googleApiKey = googleApiKey!!
        EnrollSDK.localizationCode = localizationCode
        EnrollSDK.enrollCallback = EnrollCallback
        EnrollSDK.enrollMode = EnrollMode
    }

    fun launch(
        activity: Activity,
    ) {
        if (EnrollSDK.tenantId.isEmpty())
            throw Exception("Invalid tenant id")
        if (EnrollSDK.tenantSecret.isEmpty())
            throw Exception("Invalid tenant secret")
        setLocale(EnrollSDK.localizationCode, activity)
        activity.startActivity(Intent(activity, EnrollMainActivity::class.java))
    }

    private fun setLocale(lang: LocalizationCode, activity: Activity) {
        val locale = if (lang != LocalizationCode.AR) {
            Locale("en")
        } else {
            Locale("ar")
        }

        val config: Configuration = activity.getBaseContext().getResources().getConfiguration()
        config.setLocale(locale)
        activity.getBaseContext().getResources().updateConfiguration(
            config,
            activity.getBaseContext().getResources().getDisplayMetrics()
        )
    }
}