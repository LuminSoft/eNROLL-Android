package com.luminsoft.enroll_sdk.core.sdk

import com.luminsoft.enroll_sdk.core.models.EnrollCallback
import com.luminsoft.enroll_sdk.core.models.EnrollEnvironment
import com.luminsoft.enroll_sdk.core.models.EnrollForcedDocumentType
import com.luminsoft.enroll_sdk.core.models.EnrollMode
import com.luminsoft.enroll_sdk.core.models.LocalizationCode
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.EkycStepType
import com.luminsoft.enroll_sdk.ui_components.theme.AppColors
import com.luminsoft.enroll_sdk.ui_components.theme.AppIcons
import com.luminsoft.enroll_sdk.ui_components.theme.AppTheme


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
    var appIcons = AppIcons()
    var appTheme: AppTheme
        get() = AppTheme(colors = appColors, icons = appIcons)
        set(value) {
            appColors = value.colors
            appIcons = value.icons
        }
    var fontResource = 0

    var enrollCallback: EnrollCallback? = null
    var enrollMode: EnrollMode = EnrollMode.ONBOARDING
    var enrollForcedDocumentType: EnrollForcedDocumentType? =
        EnrollForcedDocumentType.NATIONAL_ID_OR_PASSPORT
    
    // Exit step configuration - SDK will close after this step is completed
    // When set, SDK returns control to host app after completing this step
    var exitStep: EkycStepType? = null


    // Internal debug flag for LOCAL environment (not exposed to clients)
    @JvmField
    internal var isLocalDebugMode = false
    
    /**
     * Enable LOCAL environment for internal debugging only.
     * WARNING: This is for INTERNAL TESTING ONLY.
     * Should NEVER be used in production client apps.
     * This method is intentionally undocumented in public SDK documentation.
     */
    @JvmStatic
    fun enableLocalDebugMode() {
        isLocalDebugMode = true
    }
    
    /**
     * Disable LOCAL environment debug mode
     * WARNING: This is for INTERNAL TESTING ONLY.
     */
    @JvmStatic
    fun disableLocalDebugMode() {
        isLocalDebugMode = false
    }
    
    private fun getLuminBaseUrl(): String {
        return when {
            isLocalDebugMode -> "http://197.168.1.39"
            environment == EnrollEnvironment.STAGING -> "https://enrollstg.luminsoft.net"
            environment == EnrollEnvironment.PRODUCTION -> "https://enrollgateway.luminsoft.net"
            else -> "https://enrollstg.luminsoft.net" // fallback to staging
        }
    }

    fun getApisUrl(): String {
        return when {
            isLocalDebugMode -> getLuminBaseUrl() + ":4800/"
            environment == EnrollEnvironment.STAGING -> getLuminBaseUrl() + ":7400/SecureOnBoarding/"
            environment == EnrollEnvironment.PRODUCTION -> getLuminBaseUrl() + ":443/SecureOnBoarding/"
            else -> getLuminBaseUrl() + ":7400/SecureOnBoarding/" // fallback to staging
        }
    }

    fun getImageUrl(): String {
        return when {
            isLocalDebugMode -> getLuminBaseUrl() + ":4800/api/v1/onboarding/Image/GetNationalIdPhotoImage"
            environment == EnrollEnvironment.STAGING -> getLuminBaseUrl() + ":7400/OnBoarding/api/v1/onboarding/Image/GetNationalIdPhotoImage"
            environment == EnrollEnvironment.PRODUCTION -> getLuminBaseUrl() + ":443/OnBoarding/api/v1/onboarding/Image/GetNationalIdPhotoImage"
            else -> getLuminBaseUrl() + ":7400/OnBoarding/api/v1/onboarding/Image/GetNationalIdPhotoImage" // fallback to staging
        }
    }

    fun isEncryptionEnabled(): Boolean {
        return !isLocalDebugMode
    }
}
