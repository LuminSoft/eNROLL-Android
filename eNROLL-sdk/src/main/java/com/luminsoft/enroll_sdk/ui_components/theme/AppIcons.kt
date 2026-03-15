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
 * Currently supports Android drawable resources only.
 */
sealed class IconSource {
    data class Resource(@DrawableRes val resId: Int) : IconSource()
}

/**
 * Configuration for a custom icon.
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
 */
enum class LogoMode {
    DEFAULT,
    HIDDEN,
    CUSTOM
}

/**
 * Configuration for the SDK logo shown on splash screens and the app bar header.
 */
data class LogoConfig(
    val mode: LogoMode = LogoMode.DEFAULT,
    val asset: IconSource? = null,
    val renderingMode: IconRenderingMode = IconRenderingMode.ORIGINAL
)

// ---------------------------------------------------------------------------
// Business-flow icon groups — one group per SDK step
// ---------------------------------------------------------------------------

/**
 * Icons for the **Location** step.
 *
 * @param tutorial       Onboarding pager illustration.
 * @param requestAccess  Screen requesting location permission.
 * @param accessError    Screen shown when location access is denied.
 * @param grab           Screen shown while capturing / displaying location.
 */
data class LocationIcons(
    val tutorial: StepIcon? = null,
    val requestAccess: StepIcon? = null,
    val accessError: StepIcon? = null,
    val grab: StepIcon? = null,
)

/**
 * Icons for the **National ID** step.
 *
 * @param tutorial              Onboarding pager illustration (national-ID-only flow).
 * @param tutorialIdOrPassport  Onboarding pager illustration (combined ID-or-passport flow).
 * @param preScan               Pre-scan instruction screen.
 * @param scanError             Scan error / retry screen.
 * @param choose                Document-type chooser (select national ID).
 */
data class NationalIdIcons(
    val tutorial: StepIcon? = null,
    val tutorialIdOrPassport: StepIcon? = null,
    val preScan: StepIcon? = null,
    val scanError: StepIcon? = null,
    val choose: StepIcon? = null,
)

/**
 * Icons for the **Passport** step.
 *
 * @param tutorial         Onboarding pager illustration.
 * @param preScan          Pre-scan instruction screen (regular passport).
 * @param ePassportPreScan Pre-scan instruction screen (e-Passport / NFC).
 * @param choose           Document-type chooser (select passport).
 */
data class PassportIcons(
    val tutorial: StepIcon? = null,
    val preScan: StepIcon? = null,
    val ePassportPreScan: StepIcon? = null,
    val choose: StepIcon? = null,
)

/**
 * Icons for the **Phone OTP** step.
 *
 * @param tutorial     Onboarding pager illustration.
 * @param select       Phone-number selection screen.
 * @param validateOtp  OTP validation screen.
 */
data class PhoneIcons(
    val tutorial: StepIcon? = null,
    val select: StepIcon? = null,
    val validateOtp: StepIcon? = null,
)

/**
 * Icons for the **Email OTP** step.
 *
 * @param tutorial     Onboarding pager illustration.
 * @param select       Email address selection screen.
 * @param validateOtp  OTP validation screen.
 */
data class EmailIcons(
    val tutorial: StepIcon? = null,
    val select: StepIcon? = null,
    val validateOtp: StepIcon? = null,
)

/**
 * Icons for the **Face Matching / Smile Liveness** step.
 *
 * @param tutorial  Onboarding pager illustration.
 * @param preScan   Pre-scan instruction screen before face capture.
 * @param error     Capture error / retry screen.
 */
data class FaceMatchingIcons(
    val tutorial: StepIcon? = null,
    val preScan: StepIcon? = null,
    val error: StepIcon? = null,
)

/**
 * Icons for the **Security Questions** step.
 *
 * @param tutorial    Onboarding pager illustration.
 * @param authScreen  Auth / auth-update working screen.
 */
data class SecurityQuestionsIcons(
    val tutorial: StepIcon? = null,
    val authScreen: StepIcon? = null,
)

/**
 * Icons for the **Password** step.
 *
 * @param tutorial    Onboarding pager illustration.
 * @param authScreen  Auth / auth-update working screen.
 */
data class PasswordIcons(
    val tutorial: StepIcon? = null,
    val authScreen: StepIcon? = null,
)

/**
 * Icons for the **Electronic Signature** step.
 *
 * @param tutorial  Onboarding pager illustration.
 */
data class SignatureIcons(
    val tutorial: StepIcon? = null,
)

// ---------------------------------------------------------------------------
// Shared / cross-cutting icon groups
// ---------------------------------------------------------------------------

/**
 * Background images used across screens.
 */
data class BackgroundIcons(
    val main: StepIcon? = null,
    val layer1: StepIcon? = null,
    val layer2: StepIcon? = null,
    val layer3: StepIcon? = null,
    val blur: StepIcon? = null,
    val header: StepIcon? = null,
    val footer: StepIcon? = null,
)

/**
 * Popup and dialog icons.
 */
data class PopupIcons(
    val background: StepIcon? = null,
    val warningIcon: StepIcon? = null,
    val errorIcon: StepIcon? = null,
    val successIcon: StepIcon? = null,
    val errorSign: StepIcon? = null,
    val successSign: StepIcon? = null,
    val warningSign: StepIcon? = null,
)

/**
 * Profile / data display field icons (OCR confirmation screens).
 */
data class FieldIcons(
    val user: StepIcon? = null,
    val calendar: StepIcon? = null,
    val gender: StepIcon? = null,
    val issuingAuthority: StepIcon? = null,
    val nationality: StepIcon? = null,
    val num: StepIcon? = null,
    val passport: StepIcon? = null,
    val address: StepIcon? = null,
    val idCard: StepIcon? = null,
    val profession: StepIcon? = null,
    val religion: StepIcon? = null,
    val maritalStatus: StepIcon? = null,
)

/**
 * General UI icons used across screens.
 */
data class UiIcons(
    val visibility: StepIcon? = null,
    val visibilityOff: StepIcon? = null,
    val mobile: StepIcon? = null,
    val mail: StepIcon? = null,
    val answer: StepIcon? = null,
    val error: StepIcon? = null,
    val info: StepIcon? = null,
    val edit: StepIcon? = null,
    val activePhone: StepIcon? = null,
)

/**
 * Common icons shared across all flows (backgrounds, popups, fields, UI, terms).
 *
 * @param backgrounds       Background and header images.
 * @param popups            Dialog / bottom-sheet status icons.
 * @param fieldIcons        OCR confirmation field icons.
 * @param ui                Miscellaneous UI icons.
 * @param termsAndConditions Onboarding pager illustration for Terms & Conditions.
 */
data class CommonIcons(
    val backgrounds: BackgroundIcons = BackgroundIcons(),
    val popups: PopupIcons = PopupIcons(),
    val fieldIcons: FieldIcons = FieldIcons(),
    val ui: UiIcons = UiIcons(),
    val termsAndConditions: StepIcon? = null,
)

// ---------------------------------------------------------------------------
// Update / Forget mode step-list icons
// ---------------------------------------------------------------------------

/**
 * Icons shown in the **Update** step-list screen.
 */
data class UpdateIcons(
    val modeIcon: StepIcon? = null,
    val idCard: StepIcon? = null,
    val passport: StepIcon? = null,
    val mobile: StepIcon? = null,
    val email: StepIcon? = null,
    val device: StepIcon? = null,
    val address: StepIcon? = null,
    val securityQuestions: StepIcon? = null,
    val password: StepIcon? = null,
)

/**
 * Icons shown in the **Forget Profile Data** step-list screen.
 *
 * @param modeIcon           Header icon at the top of the forget screen.
 * @param nationalId         National ID forget step icon.
 * @param passport           Passport forget step icon.
 * @param phone              Phone forget step icon.
 * @param email              Email forget step icon.
 * @param device             Device forget step icon.
 * @param location           Location forget step icon.
 * @param securityQuestions  Security questions forget step icon.
 * @param password           Password forget step icon.
 */
data class ForgetIcons(
    val modeIcon: StepIcon? = null,
    val nationalId: StepIcon? = null,
    val passport: StepIcon? = null,
    val phone: StepIcon? = null,
    val email: StepIcon? = null,
    val device: StepIcon? = null,
    val location: StepIcon? = null,
    val securityQuestions: StepIcon? = null,
    val password: StepIcon? = null,
)

// ---------------------------------------------------------------------------
// Main AppIcons — groups all customizable assets by business flow
// ---------------------------------------------------------------------------

/**
 * Defines all customizable icons and images for the SDK, grouped by business step.
 *
 * All fields use defaults — when sub-fields are null the SDK renders its built-in assets.
 * Custom icons are rendered in [IconRenderingMode.ORIGINAL] mode by default (no tinting).
 *
 * Example usage:
 * ```
 * AppIcons(
 *     logo = LogoConfig(mode = LogoMode.CUSTOM, asset = IconSource.Resource(R.drawable.my_logo)),
 *     location = LocationIcons(tutorial = StepIcon(source = IconSource.Resource(R.drawable.my_loc))),
 *     nationalId = NationalIdIcons(preScan = StepIcon(source = IconSource.Resource(R.drawable.my_id))),
 * )
 * ```
 */
data class AppIcons(
    val logo: LogoConfig = LogoConfig(),
    val location: LocationIcons = LocationIcons(),
    val nationalId: NationalIdIcons = NationalIdIcons(),
    val passport: PassportIcons = PassportIcons(),
    val phone: PhoneIcons = PhoneIcons(),
    val email: EmailIcons = EmailIcons(),
    val faceMatching: FaceMatchingIcons = FaceMatchingIcons(),
    val securityQuestions: SecurityQuestionsIcons = SecurityQuestionsIcons(),
    val password: PasswordIcons = PasswordIcons(),
    val signature: SignatureIcons = SignatureIcons(),
    val common: CommonIcons = CommonIcons(),
    val update: UpdateIcons = UpdateIcons(),
    val forget: ForgetIcons = ForgetIcons(),
) {
    /**
     * Resolves the custom [StepIcon] for a given onboarding tutorial page key.
     */
    fun resolveCustomIcon(pageKey: String): StepIcon? {
        return when (pageKey) {
            "DeviceLocationPage" -> location.tutorial
            "PersonalConfirmationPage" -> nationalId.tutorial
            "PhoneOtpPage" -> phone.tutorial
            "EmailOtpPage" -> email.tutorial
            "SecurityQuestionsPage" -> securityQuestions.tutorial
            "SmileLivenessPage" -> faceMatching.tutorial
            "SettingPasswordPage" -> password.tutorial
            "ElectronicSignaturePage" -> signature.tutorial
            "TermsAndConditionsPage" -> common.termsAndConditions
            else -> null
        }
    }

    /**
     * Resolves the custom [StepIcon] for specific document scan page types
     * that share the "PersonalConfirmationPage" key but need distinct icons.
     */
    fun resolveCustomIconForPage(pageKey: String, pageClass: String): StepIcon? {
        if (pageKey == "PersonalConfirmationPage") {
            return when (pageClass) {
                "NationalIDOnlyPage" -> nationalId.tutorial
                "PassportOnlyPage" -> passport.tutorial
                "NationalIdOrPassportPage" -> nationalId.tutorialIdOrPassport
                else -> nationalId.tutorial
            }
        }
        return resolveCustomIcon(pageKey)
    }
}
