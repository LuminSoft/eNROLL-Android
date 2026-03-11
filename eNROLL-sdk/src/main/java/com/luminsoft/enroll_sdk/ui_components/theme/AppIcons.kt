package com.luminsoft.enroll_sdk.ui_components.theme

import androidx.annotation.DrawableRes

/**
 * Defines how an icon should be rendered.
 *
 * [ORIGINAL] renders the icon as-is without any color modification.
 * [TEMPLATE] applies theme-color tinting to the icon.
 */
enum class IconRenderingMode {
    ORIGINAL,
    TEMPLATE
}

/**
 * Represents a source for a custom icon.
 *
 * Currently supports Android drawable resources.
 * Designed to be extended with [AssetPath] for Flutter/cross-platform support in a future version.
 */
sealed class IconSource {
    data class Resource(@DrawableRes val resId: Int) : IconSource()
}

/**
 * Configuration for a custom onboarding step icon.
 *
 * @param source The drawable resource for the custom icon.
 * @param renderingMode How the icon should be rendered. Defaults to [IconRenderingMode.ORIGINAL]
 *   so custom client assets are not tinted.
 */
data class StepIcon(
    val source: IconSource,
    val renderingMode: IconRenderingMode = IconRenderingMode.ORIGINAL
)

/**
 * Controls how the SDK logo is displayed.
 *
 * [DEFAULT] shows the built-in eNROLL logo.
 * [HIDDEN] hides the logo entirely.
 * [CUSTOM] shows a client-provided logo.
 */
enum class LogoMode {
    DEFAULT,
    HIDDEN,
    CUSTOM
}

/**
 * Configuration for the SDK logo shown on splash screens and the app bar header.
 *
 * @param mode Controls whether the default, custom, or no logo is shown.
 * @param asset The custom logo source. Only used when [mode] is [LogoMode.CUSTOM].
 * @param renderingMode How the logo should be rendered. Defaults to [IconRenderingMode.ORIGINAL].
 */
data class LogoConfig(
    val mode: LogoMode = LogoMode.DEFAULT,
    val asset: IconSource? = null,
    val renderingMode: IconRenderingMode = IconRenderingMode.ORIGINAL
)

/**
 * Defines customizable icons for the SDK.
 *
 * All fields are nullable — when null, the SDK uses its built-in default icons.
 * Custom icons are rendered in [IconRenderingMode.ORIGINAL] mode by default
 * (no tinting), unless explicitly configured otherwise.
 *
 * Scope: logo + onboarding flow step illustrations only.
 *
 * @param logo Configuration for the SDK logo (splash screens and app bar).
 * @param locationIcon Custom icon for the Location onboarding step.
 * @param scanFrontIdIcon Custom icon for the Scan Front ID onboarding step.
 * @param scanPassportIcon Custom icon for the Scan Passport onboarding step.
 * @param scanIdOrPassportIcon Custom icon for the combined ID or Passport onboarding step.
 * @param mobileIcon Custom icon for the Mobile/Phone onboarding step.
 * @param emailIcon Custom icon for the Email onboarding step.
 * @param securityQuestionsIcon Custom icon for the Security Questions onboarding step.
 * @param faceMatchingIcon Custom icon for the Face Matching onboarding step.
 * @param signatureIcon Custom icon for the Electronic Signature onboarding step.
 * @param passwordIcon Custom icon for the Password onboarding step.
 */
data class AppIcons(
    val logo: LogoConfig = LogoConfig(),
    val locationIcon: StepIcon? = null,
    val scanFrontIdIcon: StepIcon? = null,
    val scanPassportIcon: StepIcon? = null,
    val scanIdOrPassportIcon: StepIcon? = null,
    val mobileIcon: StepIcon? = null,
    val emailIcon: StepIcon? = null,
    val securityQuestionsIcon: StepIcon? = null,
    val faceMatchingIcon: StepIcon? = null,
    val signatureIcon: StepIcon? = null,
    val passwordIcon: StepIcon? = null
) {
    /**
     * Resolves the custom [StepIcon] for a given onboarding page key.
     *
     * The key corresponds to [OnBoardingPage.stringValue] which uniquely identifies
     * each onboarding step type. Returns null if no custom icon is configured for the key,
     * in which case the default 3-layer icon system should be used.
     *
     * Pages not in the customization scope (AML, NTRA, CSO, Terms) return null
     * and always use the default icons.
     */
    fun resolveCustomIcon(pageKey: String): StepIcon? {
        return when (pageKey) {
            "DeviceLocationPage" -> locationIcon
            "PersonalConfirmationPage" -> scanFrontIdIcon
            "PhoneOtpPage" -> mobileIcon
            "EmailOtpPage" -> emailIcon
            "SecurityQuestionsPage" -> securityQuestionsIcon
            "SmileLivenessPage" -> faceMatchingIcon
            "SettingPasswordPage" -> passwordIcon
            "ElectronicSignaturePage" -> signatureIcon
            else -> null
        }
    }

    /**
     * Resolves the custom [StepIcon] for specific document scan page types
     * that share the "PersonalConfirmationPage" key but need distinct icons.
     *
     * @param pageKey The [OnBoardingPage.stringValue].
     * @param pageClass The specific class of [OnBoardingPage] for disambiguation.
     */
    fun resolveCustomIconForPage(pageKey: String, pageClass: String): StepIcon? {
        if (pageKey == "PersonalConfirmationPage") {
            return when (pageClass) {
                "NationalIDOnlyPage" -> scanFrontIdIcon
                "PassportOnlyPage" -> scanPassportIcon
                "NationalIdOrPassportPage" -> scanIdOrPassportIcon
                else -> scanFrontIdIcon
            }
        }
        return resolveCustomIcon(pageKey)
    }
}
