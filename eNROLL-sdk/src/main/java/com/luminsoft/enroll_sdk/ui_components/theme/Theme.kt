package com.luminsoft.enroll_sdk.ui_components.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.luminsoft.enroll_sdk.core.models.LocalizationCode


private val LightColorScheme = lightColorScheme(
    primary = primary,
    secondary = secondary,
    tertiary = medium,
    background = backGround,
    onPrimary = white,
    onSurface = black,
    onSecondary = hintGrey,
    error = errorColor,
    inversePrimary = defaultColor,
    inverseSurface = defaultBorderColor,
    inverseOnSurface = defaultTextColor,
    onTertiary = unselectedDotColor,
    onSecondaryContainer = onSecondaryContainer,
    onBackground = onBackground,
    outline = textColor

)
private val DarkColorScheme = darkColorScheme(
    primary = primary,
    secondary = secondary,
    tertiary = medium,
    background = backGround,
    onPrimary = white,
    onSurface = black,
    onSecondary = hintGrey,
    error = errorColor,
    inversePrimary = defaultColor,
    inverseSurface = defaultBorderColor,
    inverseOnSurface = defaultTextColor,
    onTertiary = unselectedDotColor,
    onSecondaryContainer = onSecondaryContainer,
    onBackground = onBackground,
    outline = textColor
)

@Composable
fun EKYCsDKTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    localizationCode: LocalizationCode = LocalizationCode.EN,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            LightColorScheme
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
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

    if (localizationCode == LocalizationCode.AR) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = sdkTypography,
            content = content
        )
    } else
        MaterialTheme(
            colorScheme = colorScheme,
            typography = sdkTypographyEn,
            content = content
        )
}