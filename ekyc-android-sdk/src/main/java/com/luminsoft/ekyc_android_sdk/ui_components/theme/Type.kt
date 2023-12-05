package com.luminsoft.ekyc_android_sdk.ui_components.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
private val defaultTypography = Typography()
val sdkTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = sdkFontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = sdkFontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = sdkFontFamily),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = sdkFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = sdkFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = sdkFontFamily),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = sdkFontFamily,fontSize = 17.sp, fontWeight = FontWeight.W600),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = sdkFontFamily,fontSize = 16.sp,fontWeight = FontWeight.W600 ),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = sdkFontFamily, fontSize = 15.sp,fontWeight = FontWeight.W600),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = sdkFontFamily, fontSize =  16.sp),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = sdkFontFamily, fontSize = 14.sp),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = sdkFontFamily, fontSize = 12.sp),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = sdkFontFamily, fontWeight = FontWeight.W600, fontSize = 14.sp),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = sdkFontFamily, fontWeight = FontWeight.W500, fontSize = 13.sp),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = sdkFontFamily,fontWeight = FontWeight.W400, fontSize = 12.sp),
)