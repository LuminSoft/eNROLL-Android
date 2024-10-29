package com.luminsoft.enroll_sdk.core.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import appColors

@Composable
fun ImagesBox(
    images: List<Int>,
    modifier: Modifier = Modifier,
    contentScale: ContentScale? = null
) {
    Box {
        images.forEachIndexed { index, imageResId ->
            Image(
                modifier = modifier,
                painter = painterResource(id = imageResId),
                contentScale = contentScale ?: ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    when (index) {
                        0 -> MaterialTheme.appColors.primary
                        1 -> MaterialTheme.appColors.secondary
                        else -> MaterialTheme.appColors.primary.copy(alpha = 0.4f)
                    }
                ),
                contentDescription = "Victor Ekyc Item"
            )
        }
    }
}
