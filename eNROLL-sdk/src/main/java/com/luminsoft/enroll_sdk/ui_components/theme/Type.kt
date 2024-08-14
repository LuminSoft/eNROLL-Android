package com.luminsoft.enroll_sdk.ui_components.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK

// Set of Material typography styles to start with
private val defaultTypography = Typography()
val sdkTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(
        fontFamily = sdkFontFamily,
        color = EnrollSDK.appColors.textColor
    ),
    displayMedium = defaultTypography.displayMedium.copy(
        fontFamily = sdkFontFamily,
        color = EnrollSDK.appColors.textColor
    ),
    displaySmall = defaultTypography.displaySmall.copy(
        fontFamily = sdkFontFamily,
        color = EnrollSDK.appColors.textColor
    ),
    headlineLarge = defaultTypography.headlineLarge.copy(
        fontFamily = sdkFontFamily,
        color = EnrollSDK.appColors.textColor
    ),
    headlineMedium = defaultTypography.headlineMedium.copy(
        fontFamily = sdkFontFamily,
        color = EnrollSDK.appColors.textColor
    ),
    headlineSmall = defaultTypography.headlineSmall.copy(
        fontFamily = sdkFontFamily,
        color = EnrollSDK.appColors.textColor
    ),
    titleLarge = defaultTypography.titleLarge.copy(
        fontFamily = sdkFontFamily,
        fontSize = 17.sp,
        fontWeight = FontWeight.W600,
        color = EnrollSDK.appColors.textColor
    ),
    titleMedium = defaultTypography.titleMedium.copy(
        fontFamily = sdkFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.W600,
        color = EnrollSDK.appColors.textColor
    ),
    titleSmall = defaultTypography.titleSmall.copy(
        fontFamily = sdkFontFamily,
        fontSize = 15.sp,
        fontWeight = FontWeight.W600,
        color = EnrollSDK.appColors.textColor
    ),
    bodyLarge = defaultTypography.bodyLarge.copy(
        fontFamily = sdkFontFamily,
        fontSize = 16.sp,
        color = EnrollSDK.appColors.textColor
    ),
    bodyMedium = defaultTypography.bodyMedium.copy(
        fontFamily = sdkFontFamily,
        fontSize = 14.sp,
        color = EnrollSDK.appColors.textColor
    ),
    bodySmall = defaultTypography.bodySmall.copy(
        fontFamily = sdkFontFamily,
        fontSize = 12.sp,
        color = EnrollSDK.appColors.textColor
    ),
    labelLarge = defaultTypography.labelLarge.copy(
        fontFamily = sdkFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp,
        color = EnrollSDK.appColors.textColor
    ),
    labelMedium = defaultTypography.labelMedium.copy(
        fontFamily = sdkFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 13.sp,
        color = EnrollSDK.appColors.textColor
    ),
    labelSmall = defaultTypography.labelSmall.copy(
        fontFamily = sdkFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        color = EnrollSDK.appColors.textColor
    ),
)
val sdkTypographyEn = Typography(
    displayLarge = defaultTypography.displayLarge.copy(
        fontFamily = sdkFontFamilyEn,
        color = EnrollSDK.appColors.textColor
    ),
    displayMedium = defaultTypography.displayMedium.copy(
        fontFamily = sdkFontFamilyEn,
        color = EnrollSDK.appColors.textColor
    ),
    displaySmall = defaultTypography.displaySmall.copy(
        fontFamily = sdkFontFamilyEn,
        color = EnrollSDK.appColors.textColor
    ),
    headlineLarge = defaultTypography.headlineLarge.copy(
        fontFamily = sdkFontFamilyEn,
        color = EnrollSDK.appColors.textColor
    ),
    headlineMedium = defaultTypography.headlineMedium.copy(
        fontFamily = sdkFontFamilyEn,
        color = EnrollSDK.appColors.textColor
    ),
    headlineSmall = defaultTypography.headlineSmall.copy(
        fontFamily = sdkFontFamilyEn,
        color = EnrollSDK.appColors.textColor
    ),
    titleLarge = defaultTypography.titleLarge.copy(
        fontFamily = sdkFontFamilyEn,
        fontSize = 17.sp,
        fontWeight = FontWeight.W600,
        color = EnrollSDK.appColors.textColor
    ),
    titleMedium = defaultTypography.titleMedium.copy(
        fontFamily = sdkFontFamilyEn,
        fontSize = 16.sp,
        fontWeight = FontWeight.W600,
        color = EnrollSDK.appColors.textColor
    ),
    titleSmall = defaultTypography.titleSmall.copy(
        fontFamily = sdkFontFamilyEn,
        fontSize = 15.sp,
        fontWeight = FontWeight.W600,
        color = EnrollSDK.appColors.textColor
    ),
    bodyLarge = defaultTypography.bodyLarge.copy(
        fontFamily = sdkFontFamilyEn,
        fontSize = 16.sp,
        color = EnrollSDK.appColors.textColor
    ),
    bodyMedium = defaultTypography.bodyMedium.copy(
        fontFamily = sdkFontFamilyEn,
        fontSize = 14.sp,
        color = EnrollSDK.appColors.textColor
    ),
    bodySmall = defaultTypography.bodySmall.copy(
        fontFamily = sdkFontFamilyEn,
        fontSize = 12.sp,
        color = EnrollSDK.appColors.textColor
    ),
    labelLarge = defaultTypography.labelLarge.copy(
        fontFamily = sdkFontFamilyEn,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp,
        color = EnrollSDK.appColors.textColor
    ),
    labelMedium = defaultTypography.labelMedium.copy(
        fontFamily = sdkFontFamilyEn,
        fontWeight = FontWeight.W500,
        fontSize = 13.sp,
        color = EnrollSDK.appColors.textColor
    ),
    labelSmall = defaultTypography.labelSmall.copy(
        fontFamily = sdkFontFamilyEn,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        color = EnrollSDK.appColors.textColor
    ),
)