@file:Suppress("NAME_SHADOWING")

package com.luminsoft.enroll_sdk.ui_components.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.ui_components.theme.EnrollFontSize
import com.luminsoft.enroll_sdk.ui_components.theme.EnrollTextStyle
import com.luminsoft.enroll_sdk.ui_components.theme.appColors
import kotlin.math.max

@Composable
fun ButtonView(
    onClick: () -> Unit,
    title: String,
    color: Color = MaterialTheme.appColors.primary,
    borderColor: Color? = null,
    isEnabled: Boolean = true,
    textColor: Color = MaterialTheme.appColors.white,
    width: Double? = null,
    height: Double = 45.0,
) {
    var buttonColor = color
    var textColorF = textColor
    var borderColorF = borderColor
    var border: BorderStroke? = null

    if (!isEnabled) {
        buttonColor = color.copy(alpha = 0.5f)
    }

    if (!isEnabled) {
        textColorF = textColor.copy(alpha = 0.5f)
    }

    if (borderColorF != null) {
        if (!isEnabled) {
            borderColorF = borderColor!!.copy(alpha = 0.5f)
        }
        border = BorderStroke(1.dp, borderColorF)

    }

    val presetMinimumHeight = when (
        EnrollSDK.typography?.fontSize ?: EnrollFontSize.SMALL
    ) {
        EnrollFontSize.SMALL -> 42.0
        EnrollFontSize.MEDIUM -> 44.0
        EnrollFontSize.LARGE -> 48.0
    }
    val buttonModifier = Modifier
        .fillMaxWidth()
        .then(if (width != null) Modifier.width(width.dp) else Modifier)
        .heightIn(min = max(height, presetMinimumHeight).dp)

    Button(
        enabled = isEnabled,
        onClick = onClick,
        border = border,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = buttonModifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            disabledContainerColor = buttonColor
        ),


        ) {
        EnrollText(
            text = title,
            style = EnrollTextStyle.BUTTON,
            color = textColorF,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 2
        )

    }
}
