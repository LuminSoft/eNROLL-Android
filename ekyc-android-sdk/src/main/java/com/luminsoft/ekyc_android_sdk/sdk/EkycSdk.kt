package com.luminsoft.ekyc_android_sdk.sdk

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import com.luminsoft.ekyc_android_sdk.sdk.model.EKYCCallback
import com.luminsoft.ekyc_android_sdk.EkycMainActivity
import java.util.Locale

object EkycSdk {
    // this info related to organization
    internal var tenantId = ""
    internal var tenantSecret = ""

    // this info related to sdk initiation
    private var environment = EkycEnvironment.STAGING
    private var localizationCode = LocalizationCode.EN
    internal var ekycCallback: EKYCCallback? = null
    internal lateinit var ekycMode: EkycMode

    internal fun getBaseUrl(): String {
       return when (environment) {
            EkycEnvironment.STAGING -> "http://197.168.1.39"
            EkycEnvironment.PRODUCTION -> "https://ekyc.nasps.org.eg"
    }
    }

    internal fun getApisUrl(): String {
        return if (environment == EkycEnvironment.STAGING)
            getBaseUrl() +":4800"
        else getBaseUrl() +":4800"
    }
    @Throws(Exception::class)
    fun init(
        tenantSecret: String,
        tenantId: String,
        ekycMode: EkycMode,
        environment: EkycEnvironment = EkycEnvironment.STAGING,
        localizationCode: LocalizationCode = LocalizationCode.EN,
        ekycCallback: EKYCCallback? = null,
    ) {
        if (tenantId.isEmpty())
            throw Exception("Invalid tenant id")
        if (tenantSecret.isEmpty())
            throw Exception("Invalid tenant secret")
        EkycSdk.environment = environment
        EkycSdk.tenantSecret = tenantSecret
        EkycSdk.tenantId = tenantId
        EkycSdk.localizationCode = localizationCode
        EkycSdk.ekycCallback =ekycCallback
        EkycSdk.ekycMode =ekycMode
    }

    fun launch(
        activity: Activity,
    ) {
        if (tenantId.isEmpty())
            throw Exception("Invalid tenant id")
        if (tenantSecret.isEmpty())
            throw Exception("Invalid tenant secret")
        setLocale(localizationCode,activity)
        activity.startActivity(Intent(activity, EkycMainActivity::class.java))
    }
    fun setLocale(lang: LocalizationCode, activity: Activity) {
        val locale = if(lang != LocalizationCode.AR){
            Locale("en")
        }else{
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