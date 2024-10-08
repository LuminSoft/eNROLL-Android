package com.luminsoft.enroll_sdk.core.sdk

import com.luminsoft.enroll_sdk.core.models.EnrollCallback
import com.luminsoft.enroll_sdk.core.models.EnrollEnvironment
import com.luminsoft.enroll_sdk.core.models.EnrollMode
import com.luminsoft.enroll_sdk.core.models.LocalizationCode
import com.luminsoft.enroll_sdk.ui_components.theme.AppColors


object EnrollSDK {
    // this info related to organization
    var tenantId = ""
    var googleApiKey = ""
    var tenantSecret = ""
    var applicantId = ""
    var levelOfTrustToken = ""
    var updateSteps = arrayListOf<String>()

    // this info related to sdk initiation
    var environment = EnrollEnvironment.STAGING
    var localizationCode = LocalizationCode.EN
    var skipTutorial = false
    var appColors = AppColors()

    var enrollCallback: EnrollCallback? = null
    lateinit var enrollMode: EnrollMode

    private fun getBaseUrl(): String {
        return when (environment) {
            EnrollEnvironment.STAGING -> "https://enrollstg.nasps.org.eg"
            EnrollEnvironment.PRODUCTION -> "https://enroll.nasps.org.eg"
        }
    }

    fun getApisUrl(): String {
        return if (environment == EnrollEnvironment.STAGING)
//            getBaseUrl() + ":4800"
            getBaseUrl() + ":7400/OnBoarding/"
        else getBaseUrl() + ":7400/OnBoarding/"
    }

    fun getImageUrl(): String {
        return if (environment == EnrollEnvironment.STAGING)
            getBaseUrl() + ":4600/"
        else getBaseUrl() + ":7400/AdminPanel/"
    }
}