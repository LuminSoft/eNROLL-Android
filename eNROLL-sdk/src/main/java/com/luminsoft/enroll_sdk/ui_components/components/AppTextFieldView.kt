package com.luminsoft.enroll_sdk.ui_components.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NormalTextField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    error: String? = null,
    icon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    painter: Painter? = null,
    height: Double = 45.0,
    width: Float = 1.0f,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        focusedIndicatorColor = MaterialTheme.appColors.primary,
        unfocusedIndicatorColor = MaterialTheme.appColors.primary,
        errorIndicatorColor = MaterialTheme.appColors.errorColor,
        focusedTextColor = MaterialTheme.appColors.primary,
        cursorColor = MaterialTheme.appColors.primary,
        errorCursorColor = MaterialTheme.appColors.errorColor,
        errorTextColor = MaterialTheme.appColors.errorColor,
    ),
) {
//    var focusedBorderThickness = 1.2.dp
//    var unfocusedBorderThickness = 1.2.dp
//    if (error != null) {
//        focusedBorderThickness = 1.8.dp
//        unfocusedBorderThickness = 1.8.dp
//    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth(width)
                .height(height.dp),
            textStyle = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.appColors.primary,
                fontSize = 14.sp,

                ),
            cursorBrush = SolidColor(error?.let { MaterialTheme.appColors.errorColor }
                ?: MaterialTheme.appColors.primary),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            enabled = enabled,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = value.text,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    isError = error != null,
                    label = {
                        Text(
                            text = label,
                            color = MaterialTheme.appColors.textColor.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    leadingIcon = icon ?: painter?.let {
                        {
                            Image(
                                painter = it,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(20.dp),
                            )
                        }
                    },
                    trailingIcon = trailingIcon ?: painter?.let {
                        {
                            Image(
                                painter = it,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(30.dp),
                            )
                        }
                    },
                    colors = colors,
                    contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                        top = 0.dp,
                        bottom = 0.dp,
                        start = 1.dp
                    ),
                    container = {
                        TextFieldDefaults.ContainerBox(
                            enabled = true,
                            isError = error != null,
                            interactionSource = interactionSource,
                            colors = colors,
                        )
                    },
                )

            }
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.appColors.errorColor,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }


}