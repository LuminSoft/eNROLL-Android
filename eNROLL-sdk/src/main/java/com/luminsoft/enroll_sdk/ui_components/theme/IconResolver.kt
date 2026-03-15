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
import com.luminsoft.ekyc_android_sdk.R

private const val TAG = "IconResolver"

/** Maximum allowed width (px) for any custom icon asset. */
const val MAX_ICON_WIDTH_PX = 2048

/** Maximum allowed height (px) for any custom icon asset. */
const val MAX_ICON_HEIGHT_PX = 2048

/** Maximum estimated bitmap memory (bytes) for any custom icon asset (20 MB). */
const val MAX_ICON_MEMORY_BYTES: Long = 20L * 1024L * 1024L

/**
 * Validates that a drawable resource ID points to a resolvable, inflatable, and
 * reasonably-sized resource.
 *
 * Checks performed:
 * 1. Resource ID is non-zero.
 * 2. Resource can be resolved by name via [Resources.getResourceName].
 * 3. Resource can be inflated via [Resources.getDrawable].
 * 4. Intrinsic dimensions do not exceed [MAX_ICON_WIDTH_PX] × [MAX_ICON_HEIGHT_PX].
 * 5. Estimated ARGB_8888 bitmap memory does not exceed [MAX_ICON_MEMORY_BYTES].
 *
 * Returns the resource ID if all checks pass, or `null` with a warning log if any
 * check fails. The SDK never crashes on a bad custom icon — it silently falls back
 * to the built-in default.
 */
@Suppress("deprecation")
internal fun validateIconResource(resources: Resources, @DrawableRes resId: Int): Int? {
    if (resId == 0) {
        Log.w(TAG, "Invalid icon resource ID: 0. Falling back to default.")
        return null
    }
    return try {
        resources.getResourceName(resId)
        val drawable = resources.getDrawable(resId, null)

        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight
        if (w > 0 && h > 0) {
            if (w > MAX_ICON_WIDTH_PX || h > MAX_ICON_HEIGHT_PX) {
                Log.w(
                    TAG,
                    "Custom icon too large (${w}x${h} px, max ${MAX_ICON_WIDTH_PX}x${MAX_ICON_HEIGHT_PX}). " +
                            "Falling back to default (resId=$resId)."
                )
                return null
            }
            val estimatedBytes = w.toLong() * h.toLong() * 4L
            if (estimatedBytes > MAX_ICON_MEMORY_BYTES) {
                Log.w(
                    TAG,
                    "Custom icon exceeds memory limit (${estimatedBytes / 1024}KB, max ${MAX_ICON_MEMORY_BYTES / 1024}KB). " +
                            "Falling back to default (resId=$resId)."
                )
                return null
            }
        }
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
 * The custom resource is validated (existence, inflatability, dimensions, memory)
 * via [validateIconResource] before rendering. If validation fails the
 * [defaultContent] is shown and a warning is logged.
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
 * Renders a single custom icon or falls back to the default single-image rendering.
 * Use this for all single-drawable sites (field icons, UI icons, etc.).
 *
 * @param customIcon Optional custom icon override.
 * @param defaultResId The default drawable resource ID.
 * @param contentDescription Accessibility description.
 * @param modifier Modifier for the image.
 * @param contentScale How the image should be scaled.
 * @param defaultColorFilter ColorFilter applied to the default image (ignored for custom ORIGINAL mode).
 */
@Composable
fun ResolvedImage(
    customIcon: StepIcon?,
    @DrawableRes defaultResId: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    defaultColorFilter: ColorFilter? = null
) {
    if (customIcon != null) {
        val resources = LocalContext.current.resources
        val validResId = remember(customIcon.source) {
            resolveIconSource(resources, customIcon.source)
        }
        if (validResId != null) {
            val colorFilter = when (customIcon.renderingMode) {
                IconRenderingMode.TEMPLATE -> defaultColorFilter
                IconRenderingMode.ORIGINAL -> null
            }
            Image(
                painter = painterResource(id = validResId),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale,
                colorFilter = colorFilter
            )
            return
        }
    }
    Image(
        painter = painterResource(id = defaultResId),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        colorFilter = defaultColorFilter
    )
}

/**
 * Returns a resolved Painter — custom if valid, otherwise default.
 * Use when you need the Painter directly (e.g., for Icon() composable or custom layouts).
 */
@Composable
fun resolvedPainter(customIcon: StepIcon?, @DrawableRes defaultResId: Int): androidx.compose.ui.graphics.painter.Painter {
    if (customIcon != null) {
        val resources = LocalContext.current.resources
        val validResId = remember(customIcon.source) {
            resolveIconSource(resources, customIcon.source)
        }
        if (validResId != null) {
            return painterResource(id = validResId)
        }
    }
    return painterResource(id = defaultResId)
}

/**
 * Resolves a field icon drawable ID to its corresponding custom [StepIcon] from the theme.
 * Returns null if no custom icon is configured for the given drawable.
 */
@Composable
fun resolveFieldIcon(@DrawableRes drawableId: Int): StepIcon? {
    val fields = MaterialTheme.appIcons.common.fieldIcons
    return when (drawableId) {
        R.drawable.user_icon -> fields.user
        R.drawable.calendar_icon -> fields.calendar
        R.drawable.gender_icon -> fields.gender
        R.drawable.nationality_icon -> fields.nationality
        R.drawable.factory_num_icon -> fields.num
        R.drawable.address_icon -> fields.address
        R.drawable.id_card_icon -> fields.idCard
        R.drawable.passport_icon -> fields.passport
        R.drawable.issuing_authurity_icon -> fields.issuingAuthority
        R.drawable.profession_icon -> fields.profession
        R.drawable.religion_icon -> fields.religion
        R.drawable.marital_status_icon -> fields.maritalStatus
        else -> null
    }
}

/**
 * Resolves a general UI icon drawable ID to its corresponding custom [StepIcon] from the theme.
 * Returns null if no custom icon is configured for the given drawable.
 */
@Composable
fun resolveUiIcon(@DrawableRes drawableId: Int): StepIcon? {
    val ui = MaterialTheme.appIcons.common.ui
    return when (drawableId) {
        R.drawable.visibility_icon -> ui.visibility
        R.drawable.visibility_off_icon -> ui.visibilityOff
        R.drawable.mobile_icon -> ui.mobile
        R.drawable.mail_icon -> ui.mail
        R.drawable.answer_icon -> ui.answer
        R.drawable.error_icon -> ui.error
        R.drawable.info_icon -> ui.info
        R.drawable.edit_icon -> ui.edit
        R.drawable.active_phone -> ui.activePhone
        else -> null
    }
}

/**
 * Resolves an update-mode step icon drawable ID to its corresponding custom [StepIcon].
 */
@Composable
fun resolveUpdateStepIcon(@DrawableRes drawableId: Int): StepIcon? {
    val icons = MaterialTheme.appIcons.update
    return when (drawableId) {
        R.drawable.update_id_card_icon -> icons.idCard
        R.drawable.update_passport -> icons.passport
        R.drawable.update_mobile_icon -> icons.mobile
        R.drawable.update_mail_icon -> icons.email
        R.drawable.update_device_icon -> icons.device
        R.drawable.update_address_icon -> icons.address
        R.drawable.update_answer_icon -> icons.securityQuestions
        R.drawable.update_password_icon -> icons.password
        R.drawable.update_icon -> icons.modeIcon
        else -> null
    }
}

/**
 * Resolves a forget-mode step icon drawable ID to its corresponding custom [StepIcon].
 */
@Composable
fun resolveForgetStepIcon(@DrawableRes drawableId: Int): StepIcon? {
    val icons = MaterialTheme.appIcons.forget
    return when (drawableId) {
        R.drawable.forget_icon -> icons.modeIcon
        R.drawable.update_id_card_icon -> icons.nationalId
        R.drawable.update_passport -> icons.passport
        R.drawable.forget_phone -> icons.phone
        R.drawable.forget_mail -> icons.email
        R.drawable.forget_device -> icons.device
        R.drawable.forget_location -> icons.location
        R.drawable.update_answer_icon -> icons.securityQuestions
        R.drawable.forget_password -> icons.password
        else -> null
    }
}

/**
 * Renders the SDK logo based on the [LogoConfig].
 *
 * - [LogoMode.DEFAULT]: renders [defaultContent] (the built-in eNROLL logo).
 * - [LogoMode.HIDDEN]: renders nothing.
 * - [LogoMode.CUSTOM]: validates the custom logo asset (existence, dimensions, memory)
 *   via [resolveIconSource], then renders it. Falls back to [defaultContent] if the
 *   asset is null, invalid, or oversized.
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
                Log.w(TAG, "Custom logo asset invalid or oversized. Falling back to default logo.")
                defaultContent()
            }
        }
    }
}
