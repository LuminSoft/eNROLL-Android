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
    var requestId = ""
    var levelOfTrustToken = ""
    var contractTemplateId = ""
    var contractParameters = ""
    var serverPublicKey = ""
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


    private fun getLuminBaseUrl(): String {
        return when (environment) {
            EnrollEnvironment.STAGING -> "https://enrollstg.luminsoft.net"
            EnrollEnvironment.PRODUCTION -> "https://enrollgateway.luminsoft.net"
        }
    }

    fun getApisUrl(): String {
        return when (environment) {
            EnrollEnvironment.STAGING -> getLuminBaseUrl() + ":7400/SecureOnBoarding/"
            EnrollEnvironment.PRODUCTION -> getLuminBaseUrl() + ":443/SecureOnBoarding/"
        }
    }

    fun getImageUrl(): String {
        return when (environment) {
            EnrollEnvironment.STAGING -> getLuminBaseUrl() + ":7400/OnBoarding/api/v1/onboarding/Image/GetNationalIdPhotoImage"
            EnrollEnvironment.PRODUCTION -> getLuminBaseUrl() + ":443/OnBoarding/api/v1/onboarding/Image/GetNationalIdPhotoImage"
        }
    }
}
