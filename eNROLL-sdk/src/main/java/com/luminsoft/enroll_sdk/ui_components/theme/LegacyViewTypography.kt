package com.luminsoft.enroll_sdk.ui_components.theme

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK

/**
 * Applies SDK typography to Android Views rendered outside Compose, including
 * third-party capture screens.
 */
fun View.applyEnrollTypography(defaultStyle: EnrollTextStyle = EnrollTextStyle.BODY) {
    when (this) {
        is Button -> applyEnrollTypography(EnrollTextStyle.BUTTON)
        is TextView -> applyEnrollTypography(defaultStyle)
    }

    if (this is ViewGroup) {
        for (index in 0 until childCount) {
            getChildAt(index).applyEnrollTypography(defaultStyle)
        }
    }
}

fun AppCompatActivity.applyEnrollActionBarTypography() {
    val actionBarTitle = title
    window.decorView.post {
        window.decorView.findTextViews()
            .filter { it.text?.toString() == actionBarTitle?.toString() }
            .forEach { it.applyEnrollTypography(EnrollTextStyle.SUB_TITLE) }
    }
}

private fun TextView.applyEnrollTypography(style: EnrollTextStyle) {
    val typography = EnrollSDK.typography ?: EnrollTypography.default
    val size = typography.fontSize.legacySizeFor(style)

    if (typography.dynamicTypeEnabled) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    } else {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, size * resources.displayMetrics.density)
    }

    resolveEnrollTypeface(context, typography.fontFamily)?.let { configuredTypeface ->
        typeface = Typeface.create(configuredTypeface, typeface?.style ?: Typeface.NORMAL)
    }
}

/**
 * Resolves the SP size to apply to a native (non-Compose) view.
 *
 * Third-party capture screens use fixed-height layouts (e.g. the document scan
 * instruction bar) that overflow and overlap at the LARGE tier, so the applied
 * size is capped at MEDIUM for native views. Arabic-specific MEDIUM reductions
 * are honored via the localization-aware size functions.
 */
private fun EnrollFontSize.legacySizeFor(style: EnrollTextStyle): Float {
    val cappedSize = if (this == EnrollFontSize.LARGE) EnrollFontSize.MEDIUM else this
    val localizationCode = EnrollSDK.localizationCode
    return when (style) {
        EnrollTextStyle.TITLE -> cappedSize.titleSp(localizationCode)
        EnrollTextStyle.SUB_TITLE,
        EnrollTextStyle.BODY -> cappedSize.bodySp(localizationCode)
        EnrollTextStyle.BUTTON -> cappedSize.buttonSp(localizationCode)
        EnrollTextStyle.INPUT -> cappedSize.inputSp(localizationCode)
    }
}

private fun resolveEnrollTypeface(context: Context, fontFamily: String?): Typeface? {
    val configuredFontId = fontFamily
        ?.takeIf { it.isNotBlank() }
        ?.let { context.resources.getIdentifier(it, "font", context.packageName) }
        ?.takeIf { it != 0 }

    val fontResourceId = configuredFontId ?: EnrollSDK.fontResource.takeIf { it != 0 }
    return fontResourceId?.let { ResourcesCompat.getFont(context, it) }
}

private fun View.findTextViews(): List<TextView> {
    val result = mutableListOf<TextView>()
    if (this is TextView) {
        result += this
    }
    if (this is ViewGroup) {
        for (index in 0 until childCount) {
            result += getChildAt(index).findTextViews()
        }
    }
    return result
}
