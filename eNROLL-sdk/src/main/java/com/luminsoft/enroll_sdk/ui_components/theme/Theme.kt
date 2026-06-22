package com.luminsoft.enroll_sdk.ui_components.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat
import com.luminsoft.enroll_sdk.core.models.LocalizationCode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.DynamicLocalizationManager

val LocalAppColors = staticCompositionLocalOf { AppColors() }
val LocalAppIcons = staticCompositionLocalOf { AppIcons() }


@Composable
fun EKYCsDKTheme(
    appColors: AppColors,
    appIcons: AppIcons = AppIcons(),
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    localizationCode: LocalizationCode = LocalizationCode.EN,
    content: @Composable () -> Unit
) {

    val context = LocalContext.current
    val typographyConfig = EnrollSDK.typography ?: EnrollTypography.default
    val customTypography = remember(context, localizationCode, typographyConfig, EnrollSDK.fontResource) {
        createEnrollTypography(
            context = context,
            localizationCode = localizationCode,
            typography = typographyConfig,
            legacyFontResId = EnrollSDK.fontResource
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
    val currentDensity = LocalDensity.current
    val sdkDensity = if (typographyConfig.dynamicTypeEnabled) {
        currentDensity
    } else {
        Density(currentDensity.density, fontScale = 1f)
    }
    val localizedContext = remember(context) {
        DynamicLocalizationManager.wrapContext(context)
    }

    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalDensity provides sdkDensity,
        LocalAppColors provides appColors,
        LocalAppIcons provides appIcons
    ) {
        MaterialTheme(
            colorScheme = selectedColorScheme,
            typography = customTypography,
            content = content
        )
    }


}

fun createEnrollTypography(
    context: Context,
    localizationCode: LocalizationCode,
    typography: EnrollTypography,
    legacyFontResId: Int
): Typography {
    val fontFamily = resolveEnrollFontFamily(
        context = context,
        localizationCode = localizationCode,
        typography = typography,
        legacyFontResId = legacyFontResId
    )
    val sizes = typography.fontSize.toEnrollFontSizes(localizationCode)
    val defaultTypography = Typography()

    return Typography(
        displayLarge = defaultTypography.displayLarge.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = sizes.title.sp,
            lineHeight = sizes.titleLineHeight.sp,
            color = EnrollSDK.appColors.textColor
        ),
        headlineMedium = defaultTypography.headlineMedium.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = sizes.title.sp,
            lineHeight = sizes.titleLineHeight.sp,
            color = EnrollSDK.appColors.textColor
        ),
        titleLarge = defaultTypography.titleLarge.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = sizes.title.sp,
            lineHeight = sizes.titleLineHeight.sp,
            color = EnrollSDK.appColors.textColor
        ),
        titleMedium = defaultTypography.titleMedium.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = sizes.body.sp,
            lineHeight = sizes.bodyLineHeight.sp,
            color = EnrollSDK.appColors.textColor
        ),
        titleSmall = defaultTypography.titleSmall.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = sizes.body.sp,
            lineHeight = sizes.bodyLineHeight.sp,
            color = EnrollSDK.appColors.textColor
        ),
        bodyLarge = defaultTypography.bodyLarge.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = sizes.body.sp,
            lineHeight = sizes.bodyLineHeight.sp,
            color = EnrollSDK.appColors.textColor
        ),
        bodyMedium = defaultTypography.bodyMedium.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = sizes.body.sp,
            lineHeight = sizes.bodyLineHeight.sp,
            color = EnrollSDK.appColors.textColor
        ),
        bodySmall = defaultTypography.bodySmall.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = sizes.input.sp,
            lineHeight = sizes.inputLineHeight.sp,
            color = EnrollSDK.appColors.textColor
        ),
        labelLarge = defaultTypography.labelLarge.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.W600,
            fontSize = sizes.button.sp,
            lineHeight = sizes.buttonLineHeight.sp,
            color = EnrollSDK.appColors.textColor
        ),
        labelMedium = defaultTypography.labelMedium.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.W500,
            fontSize = sizes.button.sp,
            lineHeight = sizes.buttonLineHeight.sp,
            color = EnrollSDK.appColors.textColor
        ),
        labelSmall = defaultTypography.labelSmall.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.W400,
            fontSize = sizes.input.sp,
            lineHeight = sizes.inputLineHeight.sp,
            color = EnrollSDK.appColors.textColor
        )
    )
}

private data class ResolvedEnrollFontSizes(
    val title: Int,
    val body: Int,
    val button: Int,
    val input: Int,
    val titleLineHeight: Int,
    val bodyLineHeight: Int,
    val buttonLineHeight: Int,
    val inputLineHeight: Int
)

private fun EnrollFontSize.toEnrollFontSizes(
    localizationCode: LocalizationCode
): ResolvedEnrollFontSizes {
    val title = titleSp(localizationCode).toInt()
    val body = bodySp(localizationCode).toInt()
    val button = buttonSp(localizationCode).toInt()
    val input = inputSp(localizationCode).toInt()

    return when (this) {
        EnrollFontSize.SMALL -> ResolvedEnrollFontSizes(
            title = title,
            body = body,
            button = button,
            input = input,
            titleLineHeight = 28,
            bodyLineHeight = 24,
            buttonLineHeight = 24,
            inputLineHeight = 22
        )

        EnrollFontSize.MEDIUM -> ResolvedEnrollFontSizes(
            title = title,
            body = body,
            button = button,
            input = input,
            titleLineHeight = 32,
            bodyLineHeight = 27,
            buttonLineHeight = 27,
            inputLineHeight = 25
        )

        EnrollFontSize.LARGE -> ResolvedEnrollFontSizes(
            title = title,
            body = body,
            button = button,
            input = input,
            titleLineHeight = 40,
            bodyLineHeight = 32,
            buttonLineHeight = 32,
            inputLineHeight = 30
        )
    }
}

private fun resolveEnrollFontFamily(
    context: Context,
    localizationCode: LocalizationCode,
    typography: EnrollTypography,
    legacyFontResId: Int
): FontFamily {
    val configuredFontResId = typography.fontFamily
        ?.takeIf { it.isNotBlank() }
        ?.let { fontFamilyName ->
            context.resources.getIdentifier(fontFamilyName, "font", context.packageName)
        }
        ?: 0

    return when {
        configuredFontResId != 0 -> FontFamily(Font(configuredFontResId, FontWeight.Normal))
        legacyFontResId != 0 -> FontFamily(Font(legacyFontResId, FontWeight.Normal))
        localizationCode == LocalizationCode.EN -> sdkFontFamilyEn
        else -> sdkFontFamily
    }
}

val MaterialTheme.appColors: AppColors
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current

val MaterialTheme.appIcons: AppIcons
    @Composable
    @ReadOnlyComposable
    get() = LocalAppIcons.current
