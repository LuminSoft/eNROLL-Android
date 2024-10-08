package com.luminsoft.enroll_sdk.ui_components.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.luminsoft.ekyc_android_sdk.R

val sdkFontFamily = FontFamily(
    fonts = arrayOf(
        Font(
            resId = R.font.ge_flow_bold,
            weight = FontWeight.W900,
            FontStyle.Normal
        ),
        Font(
            resId = R.font.ge_flow_regular,
            weight = FontWeight.W500,
            FontStyle.Normal
        ),
        Font(
            resId = R.font.ge_flow_bold,
            weight = FontWeight.W600,
            FontStyle.Normal
        ),
        Font(
            resId = R.font.ge_flow_regular,
            weight = FontWeight.W400,
            FontStyle.Normal
        ),
    )
)

val sdkFontFamilyEn = FontFamily(
    fonts = arrayOf(
        Font(
            resId = R.font.cairo_bold,
            weight = FontWeight.W900,
            FontStyle.Normal
        ),
        Font(
            resId = R.font.cairo_regular,
            weight = FontWeight.W500,
            FontStyle.Normal
        ),
        Font(
            resId = R.font.cairo_bold,
            weight = FontWeight.W600,
            FontStyle.Normal
        ),
        Font(
            resId = R.font.cairo_regular,
            weight = FontWeight.W400,
            FontStyle.Normal
        ),
    )
)
