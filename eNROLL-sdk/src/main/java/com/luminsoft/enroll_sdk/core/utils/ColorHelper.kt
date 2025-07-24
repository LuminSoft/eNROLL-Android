package com.luminsoft.enroll_sdk.core.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun lightenColor(color: Color, factor: Float): Color {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(color.toArgb(), hsl)
    hsl[2] = (hsl[2] + factor).coerceIn(0f, 1f) // Lightness
    return Color(ColorUtils.HSLToColor(hsl))
}


fun darkenColor(color: Color, factor: Float): Color {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(color.toArgb(), hsl)
    hsl[2] = (hsl[2] - factor).coerceIn(0f, 1f) // Lightness
    return Color(ColorUtils.HSLToColor(hsl))
}