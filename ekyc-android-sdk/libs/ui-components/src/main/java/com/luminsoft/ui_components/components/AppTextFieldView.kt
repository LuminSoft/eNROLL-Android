package com.luminsoft.ui_components.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NormalTextField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    error: String? = null,
    icon: @Composable (() -> Unit)? = null,
    painter: Painter?= null,
    height: Double = 45.0,
    keyboardOptions:KeyboardOptions = KeyboardOptions.Default,
    keyboardActions:KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.primary,
        focusedContainerColor = Color.White,
        unfocusedTextColor = MaterialTheme.colorScheme.primary,
        unfocusedContainerColor = Color.White,
        errorContainerColor = Color.White,
        disabledTextColor = Color.Transparent,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
        disabledIndicatorColor = Color.Transparent ,
        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.error,
    ),
    shape: Shape = RoundedCornerShape(12.dp)
) {
    var focusedBorderThickness = 1.2.dp
    var unfocusedBorderThickness = 1.2.dp
    if (error != null) {
        focusedBorderThickness = 1.8.dp
        unfocusedBorderThickness = 1.8.dp
    }
    Column( modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp)) {
        BasicTextField(
            value = value,
            onValueChange =onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp)
            ,
            textStyle = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary),
            cursorBrush =  SolidColor(error?.let { MaterialTheme.colorScheme.error}?:MaterialTheme.colorScheme.primary) ,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions =keyboardActions,
            interactionSource = interactionSource,
            singleLine = true,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.OutlinedTextFieldDecorationBox(
                    value = value.text,
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    label = {
                        Text(
                            text = label,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    contentPadding = PaddingValues(4.dp),
                    leadingIcon = icon?:
                    painter?.let {
                        {
                            Image(
                                painter = it,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(20.dp),
                            )
                        }
                    },
                    singleLine = true,
                    isError = error != null,
                    interactionSource = interactionSource,
                    colors = colors,
                    enabled = true,
                    container = {
                        TextFieldDefaults.OutlinedBorderContainerBox(
                            enabled = true,
                            isError = error != null,
                            interactionSource,
                            colors,
                            shape = shape,
                            focusedBorderThickness = focusedBorderThickness,
                            unfocusedBorderThickness = unfocusedBorderThickness
                        )
                    }
                )

            }
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }


}