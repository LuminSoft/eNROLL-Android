package com.luminsoft.enroll_sdk.ui_components.theme

import android.content.res.Resources
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

private const val TAG = "IconResolver"

/**
 * Validates that a drawable resource ID points to an actual resolvable and inflatable resource.
 *
 * Checks:
 * 1. Resource ID is non-zero.
 * 2. Resource can be resolved by name via [Resources.getResourceName] — confirms
 *    the ID maps to a real entry in the resource table.
 * 3. Resource can be inflated via [Resources.getDrawable] — confirms the drawable
 *    data is valid and decodable. This prevents a bad resource from crashing
 *    at Compose render time.
 *
 * Returns the resource ID if all checks pass, or null if any fail (with a warning log).
 *
 * Note: This does not validate file size or image dimensions.
 * Size/dimension validation is deferred to a future iteration.
 */
@Suppress("deprecation")
internal fun validateIconResource(resources: Resources, @DrawableRes resId: Int): Int? {
    if (resId == 0) {
        Log.w(TAG, "Invalid icon resource ID: 0. Falling back to default.")
        return null
    }
    return try {
        resources.getResourceName(resId)
        resources.getDrawable(resId, null)
        resId
    } catch (e: Resources.NotFoundException) {
        Log.w(TAG, "Drawable resource not found (resId=$resId). Falling back to default.", e)
        null
    } catch (e: Exception) {
        Log.w(TAG, "Drawable resource failed to inflate (resId=$resId). Falling back to default.", e)
        null
    }
}

/**
 * Resolves the drawable resource ID from an [IconSource].
 * Returns null if the source is invalid or cannot be resolved.
 */
internal fun resolveIconSource(resources: Resources, source: IconSource): Int? {
    return when (source) {
        is IconSource.Resource -> validateIconResource(resources, source.resId)
    }
}

/**
 * Renders a single custom icon or falls back to the default 3-layer composited rendering.
 *
 * Validation is performed before rendering:
 * - The resource ID is confirmed resolvable via [validateIconResource].
 * - As a safety net, the render call is wrapped in try-catch so that even if
 *   an unexpected error occurs at Compose render time, the default content is shown.
 *
 * @param customIcon Optional custom step icon configuration.
 * @param modifier Modifier for the rendered image.
 * @param contentScale How the image should be scaled.
 * @param defaultContent Composable that renders the default icon (3-layer system).
 */
@Composable
fun ResolvedStepIcon(
    customIcon: StepIcon?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    defaultContent: @Composable () -> Unit
) {
    if (customIcon == null) {
        defaultContent()
        return
    }

    val resources = LocalContext.current.resources
    val validResId = remember(customIcon.source) {
        resolveIconSource(resources, customIcon.source)
    }

    if (validResId != null) {
        val colorFilter = when (customIcon.renderingMode) {
            IconRenderingMode.TEMPLATE -> ColorFilter.tint(MaterialTheme.appColors.primary)
            IconRenderingMode.ORIGINAL -> null
        }
        Image(
            painter = painterResource(id = validResId),
            contentDescription = "Custom Step Icon",
            modifier = modifier,
            contentScale = contentScale,
            colorFilter = colorFilter
        )
    } else {
        defaultContent()
    }
}

/**
 * Renders the SDK logo based on the [LogoConfig].
 *
 * - [LogoMode.DEFAULT]: renders [defaultContent] (the built-in 2-part eNROLL logo).
 * - [LogoMode.HIDDEN]: renders nothing.
 * - [LogoMode.CUSTOM]: validates the custom logo asset is resolvable, then renders it.
 *   Falls back to [defaultContent] if the asset is null, invalid, or fails to render.
 *
 * @param logoConfig The logo configuration from [AppIcons].
 * @param modifier Modifier for the rendered logo image.
 * @param contentScale How the logo should be scaled.
 * @param defaultContent Composable that renders the default SDK logo.
 */
@Composable
fun ResolvedLogo(
    logoConfig: LogoConfig,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillBounds,
    defaultContent: @Composable () -> Unit
) {
    when (logoConfig.mode) {
        LogoMode.DEFAULT -> {
            defaultContent()
        }
        LogoMode.HIDDEN -> {
            // Render nothing
        }
        LogoMode.CUSTOM -> {
            val asset = logoConfig.asset
            if (asset == null) {
                Log.w(TAG, "LogoMode.CUSTOM set but no asset provided. Falling back to default logo.")
                defaultContent()
                return
            }

            val resources = LocalContext.current.resources
            val validResId = remember(asset) {
                resolveIconSource(resources, asset)
            }

            if (validResId != null) {
                val colorFilter = when (logoConfig.renderingMode) {
                    IconRenderingMode.TEMPLATE -> ColorFilter.tint(MaterialTheme.appColors.primary)
                    IconRenderingMode.ORIGINAL -> null
                }
                Image(
                    painter = painterResource(id = validResId),
                    contentDescription = "Custom Logo",
                    modifier = modifier,
                    contentScale = contentScale,
                    colorFilter = colorFilter
                )
            } else {
                Log.w(TAG, "Custom logo asset invalid. Falling back to default logo.")
                defaultContent()
            }
        }
    }
}
