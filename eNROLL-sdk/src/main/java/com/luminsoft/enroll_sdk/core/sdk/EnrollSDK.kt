package com.luminsoft.enroll_sdk.core.sdk

import com.luminsoft.enroll_sdk.core.models.EnrollCallback
import com.luminsoft.enroll_sdk.core.models.EnrollEnvironment
import com.luminsoft.enroll_sdk.core.models.EnrollMode
import com.luminsoft.enroll_sdk.core.models.LocalizationCode


object EnrollSDK {
    // this info related to organization
    var tenantId = ""
    var googleApiKey = ""
    var tenantSecret = ""

    // this info related to sdk initiation
    var environment = EnrollEnvironment.STAGING
    var localizationCode = LocalizationCode.EN
    var enrollCallback: EnrollCallback? = null
    lateinit var ekycMode: EnrollMode

    private fun getBaseUrl(): String {
        return when (environment) {
            EnrollEnvironment.STAGING -> "http://197.44.231.206"
//            EkycEnvironment.STAGING -> "http://197.168.1.39"
            EnrollEnvironment.PRODUCTION -> "https://ekyc.nasps.org.eg"
        }
    }

    fun getApisUrl(): String {
        return if (environment == EnrollEnvironment.STAGING)
            getBaseUrl() + ":4800"
        else getBaseUrl() + ":4800"
    }


}