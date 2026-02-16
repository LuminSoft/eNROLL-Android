package com.luminsoft.enroll_sdk.innovitices.nfcreading.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.SpinKitLoadingIndicator
import com.luminsoft.enroll_sdk.ui_components.theme.appColors

@Composable
fun NfcScanningScreen(
    isScanning: Boolean = false,
    onCancel: () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "nfc_pulse")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.appColors.backGround)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.appColors.primary)
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.nfc_scanning_title),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Animated NFC icon with pulse
            Box(contentAlignment = Alignment.Center) {
                // Pulse ring
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .scale(pulseScale)
                        .alpha(pulseAlpha)
                        .background(
                            MaterialTheme.appColors.primary.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                )
                // Icon background
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            MaterialTheme.appColors.primary.copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.passport_scan_01),
                        contentDescription = "NFC",
                        colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary),
                        modifier = Modifier.size(60.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            if (isScanning) {
                // Processing state - shown after NFC read succeeds
                Text(
                    text = stringResource(id = R.string.nfc_processing_data),
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.appColors.primary,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(24.dp))
                SpinKitLoadingIndicator()
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(id = R.string.nfc_processing_wait),
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                    fontSize = 14.sp,
                    color = MaterialTheme.appColors.appBlack.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                )
            } else {
                // Waiting state - hold passport near device
                Text(
                    text = stringResource(id = R.string.nfc_reading_in_progress),
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.appColors.primary,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(id = R.string.nfc_hold_passport),
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                    fontSize = 14.sp,
                    color = MaterialTheme.appColors.appBlack.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                )
            }
        }

        // Cancel button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            ButtonView(
                onClick = onCancel,
                title = stringResource(id = R.string.cancel),
                color = MaterialTheme.appColors.backGround,
                borderColor = MaterialTheme.appColors.primary,
                textColor = MaterialTheme.appColors.primary,
            )
        }
    }
}
