package com.luminsoft.enroll_sdk.core.sdk

import com.luminsoft.enroll_sdk.core.models.EnrollCallback
import com.luminsoft.enroll_sdk.core.models.EnrollEnvironment
import com.luminsoft.enroll_sdk.core.models.EnrollForcedDocumentType
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
    var appColors = AppColors()
    var fontResource = 0

    var enrollCallback: EnrollCallback? = null
    var enrollMode: EnrollMode = EnrollMode.ONBOARDING
    var enrollForcedDocumentType: EnrollForcedDocumentType? =
        EnrollForcedDocumentType.NATIONAL_ID_OR_PASSPORT

    private fun getBaseUrl(): String {
        return when (environment) {
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
            getBaseUrl() + ":4800/api/v1/onboarding/Image/GetNationalIdPhotoImage"
        else getBaseUrl() + ":7400/OnBoarding/api/v1/onboarding/Image/GetNationalIdPhotoImage"
    }
}