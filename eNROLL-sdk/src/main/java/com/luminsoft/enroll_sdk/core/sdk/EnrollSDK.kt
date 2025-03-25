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
    var correlationId = ""
    var levelOfTrustToken = ""
    var updateSteps = arrayListOf<String>()

    // this info related to sdk initiation
    var environment = EnrollEnvironment.STAGING
    var localizationCode = LocalizationCode.AR
    var skipTutorial = false
    var egyptianNationalId = false
    var appColors = AppColors()
    var fontResource = 0

    var enrollCallback: EnrollCallback? = null
    var enrollMode: EnrollMode = EnrollMode.ONBOARDING

    private fun getBaseUrl(): String {
        return when (environment) {
            EnrollEnvironment.STAGING -> "https://enrollstg.nasps.org.eg"
            EnrollEnvironment.PRODUCTION -> "https://enroll.nasps.org.eg"
        }
    }

    fun getApisUrl(): String {
        return getBaseUrl() + ":7400/OnBoarding/"
    }

    fun getImageUrl(): String {
        return getBaseUrl() + ":7400/AdminPanel/"
    }
}