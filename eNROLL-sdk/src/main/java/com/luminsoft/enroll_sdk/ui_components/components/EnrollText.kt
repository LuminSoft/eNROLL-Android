package com.luminsoft.enroll_sdk.ui_components.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.luminsoft.enroll_sdk.ui_components.theme.EnrollTextStyle
import com.luminsoft.enroll_sdk.ui_components.theme.appColors

@Composable
fun EnrollText(
    text: String,
    style: EnrollTextStyle = EnrollTextStyle.BODY,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.appColors.textColor,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        style = enrollTextStyle(style).copy(
            platformStyle = PlatformTextStyle(includeFontPadding = true),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            )
        ),
        color = color,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun enrollTextStyle(style: EnrollTextStyle): TextStyle {
    return when (style) {
        EnrollTextStyle.TITLE -> MaterialTheme.typography.titleLarge
        EnrollTextStyle.SUB_TITLE -> MaterialTheme.typography.titleMedium
        EnrollTextStyle.BODY -> MaterialTheme.typography.bodyMedium
        EnrollTextStyle.BUTTON -> MaterialTheme.typography.labelLarge
        EnrollTextStyle.INPUT -> MaterialTheme.typography.bodySmall
    }
}
