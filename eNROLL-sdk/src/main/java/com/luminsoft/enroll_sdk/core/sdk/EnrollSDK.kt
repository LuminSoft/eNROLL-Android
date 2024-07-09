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
    var applicantId = ""
    var levelOfTrustToken = ""

    // this info related to sdk initiation
    var environment = EnrollEnvironment.STAGING
    var localizationCode = LocalizationCode.EN
    var enrollCallback: EnrollCallback? = null
    lateinit var enrollMode: EnrollMode

    private fun getBaseUrl(): String {
        return when (environment) {
//            EnrollEnvironment.STAGING -> "http://197.44.231.206"
            EnrollEnvironment.STAGING -> "http://197.168.1.39"
            EnrollEnvironment.PRODUCTION -> "https://enroll.nasps.org.eg"
        }
    }

    fun getApisUrl(): String {
        return if (environment == EnrollEnvironment.STAGING)
            getBaseUrl() + ":4800"
        else getBaseUrl() + ":7400/OnBoarding/"
    }

    fun getImageUrl(): String {
        return if (environment == EnrollEnvironment.STAGING)
            getBaseUrl() + ":4600/"
        else getBaseUrl() + ":7400/AdminPanel/"
    }
}