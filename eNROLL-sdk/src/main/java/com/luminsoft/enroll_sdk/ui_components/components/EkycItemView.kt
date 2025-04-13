package com.luminsoft.enroll_sdk.ui_components.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.luminsoft.enroll_sdk.core.widgets.ImagesBox


@Composable
fun EnrollItemView(  images: List<Int>,textResourceId: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        ImagesBox(images = images,  modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.5f),)
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = textResourceId),
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

