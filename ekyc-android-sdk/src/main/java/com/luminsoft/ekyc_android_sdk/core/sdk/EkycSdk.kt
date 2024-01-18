package com.luminsoft.ekyc_android_sdk.core.sdk

import com.luminsoft.ekyc_android_sdk.core.models.EKYCCallback
import com.luminsoft.ekyc_android_sdk.core.models.EkycEnvironment
import com.luminsoft.ekyc_android_sdk.core.models.EkycMode
import com.luminsoft.ekyc_android_sdk.core.models.LocalizationCode


object EkycSdk {
    // this info related to organization
    var tenantId = ""
    var tenantSecret = ""

    // this info related to sdk initiation
    var environment = EkycEnvironment.STAGING
    var localizationCode = LocalizationCode.EN
    var ekycCallback: EKYCCallback? = null
    lateinit var ekycMode: EkycMode

    private fun getBaseUrl(): String {
        return when (environment) {
            EkycEnvironment.STAGING -> "http://197.44.231.206"
//            EkycEnvironment.STAGING -> "http://197.168.1.39"
            EkycEnvironment.PRODUCTION -> "https://ekyc.nasps.org.eg"
        }
    }

    fun getApisUrl(): String {
        return if (environment == EkycEnvironment.STAGING)
            getBaseUrl() + ":4800"
        else getBaseUrl() + ":4800"
    }


}