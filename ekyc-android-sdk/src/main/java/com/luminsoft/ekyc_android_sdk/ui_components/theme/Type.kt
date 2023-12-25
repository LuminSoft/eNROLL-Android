package com.luminsoft.ekyc_android_sdk.ui_components.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
private val defaultTypography = Typography()
val sdkTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = sdkFontFamily, color =  textColor),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = sdkFontFamily,color =  textColor),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = sdkFontFamily,color =  textColor),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = sdkFontFamily,color =  textColor),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = sdkFontFamily,color =  textColor),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = sdkFontFamily,color =  textColor),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = sdkFontFamily,fontSize = 17.sp, fontWeight = FontWeight.W600,color =  textColor),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = sdkFontFamily,fontSize = 16.sp,fontWeight = FontWeight.W600,color =  textColor ),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = sdkFontFamily, fontSize = 15.sp,fontWeight = FontWeight.W600,color =  textColor),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = sdkFontFamily, fontSize =  16.sp,color =  textColor),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = sdkFontFamily, fontSize = 14.sp,color =  textColor),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = sdkFontFamily, fontSize = 12.sp,color =  textColor),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = sdkFontFamily, fontWeight = FontWeight.W600, fontSize = 14.sp,color =  textColor),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = sdkFontFamily, fontWeight = FontWeight.W500, fontSize = 13.sp,color =  textColor),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = sdkFontFamily,fontWeight = FontWeight.W400, fontSize = 12.sp,color =  textColor),
)