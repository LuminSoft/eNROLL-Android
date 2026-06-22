package com.luminsoft.enroll_sdk.ui_components.theme

import com.luminsoft.enroll_sdk.core.models.LocalizationCode

/**
 * Unified theme configuration for the eNROLL SDK.
 *
 * Groups color and icon customization under a single concept,
 * aligned with the iOS SDK's unified theme approach.
 *
 * @param colors Color customization for the SDK UI.
 * @param icons Icon customization for logo and onboarding step illustrations.
 * @param typography Text, font, and localization customization for SDK UI.
 */
data class AppTheme(
    val colors: AppColors = AppColors(),
    val icons: AppIcons = AppIcons(),
    val typography: EnrollTypography? = null
)

enum class EnrollTextStyle {
    TITLE,
    SUB_TITLE,
    BODY,
    BUTTON,
    INPUT
}

enum class EnrollFontSize {
    SMALL,
    MEDIUM,
    LARGE
}

val EnrollFontSize.titleSp: Float
    get() = when (this) {
        EnrollFontSize.SMALL -> 20f
        EnrollFontSize.MEDIUM -> 24f
        EnrollFontSize.LARGE -> 30f
    }

val EnrollFontSize.bodySp: Float
    get() = when (this) {
        EnrollFontSize.SMALL -> 16f
        EnrollFontSize.MEDIUM -> 18f
        EnrollFontSize.LARGE -> 22f
    }

val EnrollFontSize.buttonSp: Float
    get() = when (this) {
        EnrollFontSize.SMALL -> 17f
        EnrollFontSize.MEDIUM -> 19f
        EnrollFontSize.LARGE -> 24f
    }

val EnrollFontSize.inputSp: Float
    get() = when (this) {
        EnrollFontSize.SMALL -> 15f
        EnrollFontSize.MEDIUM -> 17f
        EnrollFontSize.LARGE -> 21f
    }

/**
 * Arabic glyphs (e.g. Amiri-Naskh) render visually taller than Latin glyphs at the
 * same point size, so the MEDIUM tier reads as oversized in Arabic. These overloads
 * apply a slightly reduced MEDIUM size when the active language is Arabic, while
 * SMALL/LARGE and all Latin sizes remain unchanged.
 */
private fun EnrollFontSize.isArabicMedium(localizationCode: LocalizationCode): Boolean =
    this == EnrollFontSize.MEDIUM && localizationCode == LocalizationCode.AR

fun EnrollFontSize.titleSp(localizationCode: LocalizationCode): Float =
    if (isArabicMedium(localizationCode)) 21f else titleSp

fun EnrollFontSize.bodySp(localizationCode: LocalizationCode): Float =
    if (isArabicMedium(localizationCode)) 16f else bodySp

fun EnrollFontSize.buttonSp(localizationCode: LocalizationCode): Float =
    if (isArabicMedium(localizationCode)) 17f else buttonSp

fun EnrollFontSize.inputSp(localizationCode: LocalizationCode): Float =
    if (isArabicMedium(localizationCode)) 15f else inputSp

data class EnrollLocalizationOverrides(
    val englishFileName: String? = null,
    val arabicFileName: String? = null
)

data class EnrollTypography(
    val fontFamily: String? = null,
    val dynamicTypeEnabled: Boolean = true,
    val fontSize: EnrollFontSize = EnrollFontSize.SMALL,
    val localizationOverrides: EnrollLocalizationOverrides? = null
) {
    companion object {
        val default = EnrollTypography()
    }
}
