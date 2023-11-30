package com.luminsoft.cowpay_sdk.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ButtonView(
    onClick: () -> Unit,
    title: String,
    color: Color = MaterialTheme.colorScheme.primary,
    borderColor: Color? = null,
    isEnabled: Boolean = true,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    width: Double? = null,
    height: Double = 45.0,
    modifier: Modifier = Modifier,
) {
    var buttonColor = color
    var border :BorderStroke?= null
    var modifier = modifier

    if(!isEnabled){
        buttonColor=color.copy(alpha = 0.5f)
    }
    if(borderColor != null){
        border = BorderStroke(1.dp, borderColor)
    }

     modifier = Modifier
        .fillMaxWidth()
        .height(height.dp)

    if (width != null) {
        modifier.width(width.dp)
    }

    Button(
        enabled = isEnabled,
        onClick = onClick,
        border = border,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor, disabledContainerColor = buttonColor) ,


        ) {
        Text(text = title,style = MaterialTheme.typography.titleMedium,color= textColor)

    }
}