package com.luminsoft.enroll_sdk.ui_components.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import com.luminsoft.enroll_sdk.core.models.LocalizationCode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import androidx.compose.material3.Typography

val LocalAppColors = staticCompositionLocalOf { AppColors() }


@Composable
fun EKYCsDKTheme(
    appColors: AppColors,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    fontResource: Int,
    localizationCode: LocalizationCode = LocalizationCode.EN,
    content: @Composable () -> Unit
) {

    fun createCustomTypography(fontResId: Int): Typography {
        // Create a FontFamily using the font resource ID
        val customFontFamily = FontFamily(
            Font(fontResId, FontWeight.Normal)
        )

        // Define Typography with Material 3 using customFontFamily
        return Typography(
            displayLarge = TextStyle(
                fontFamily = customFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            ),
            headlineMedium = TextStyle(
                fontFamily = customFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp
            ),
            bodyMedium = TextStyle(
                fontFamily = customFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
            labelSmall = TextStyle(
                fontFamily = customFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp
            ),
            // Define other styles as needed
        )
    }

    val lightColorScheme = lightColorScheme(
        primary = appColors.primary,
        secondary = appColors.secondary,
        background = appColors.backGround,
        error = appColors.errorColor,
        inverseOnSurface = appColors.backGround,
    )

    val darkColorScheme = darkColorScheme(
        primary = appColors.primary,
        secondary = appColors.secondary,
        background = appColors.backGround,
        error = appColors.errorColor,
        inverseOnSurface = appColors.backGround,
    )

    val selectedColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            lightColorScheme
        }

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                darkTheme
        }
    }
    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = selectedColorScheme,
            typography =/* if (localizationCode == LocalizationCode.AR) sdkTypography else sdkTypographyEn*/createCustomTypography(
                EnrollSDK.fontResource
            ),
            content = content
        )
    }


}

val MaterialTheme.appColors: AppColors
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current
