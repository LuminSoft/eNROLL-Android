package com.luminsoft.ekyc_android_sdk.sdk

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import com.luminsoft.ekyc_android_sdk.EkycMainActivity
import com.luminsoft.ekyc_android_sdk.core.models.EKYCCallback
import com.luminsoft.ekyc_android_sdk.core.models.EkycEnvironment
import com.luminsoft.ekyc_android_sdk.core.models.EkycMode
import com.luminsoft.ekyc_android_sdk.core.models.LocalizationCode
import com.luminsoft.ekyc_android_sdk.core.sdk.EkycSdk
import java.util.Locale

object Ekyc {
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
        if (EkycSdk.tenantId.isEmpty())
            throw Exception("Invalid tenant id")
        if (EkycSdk.tenantSecret.isEmpty())
            throw Exception("Invalid tenant secret")
        setLocale(EkycSdk.localizationCode,activity)
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