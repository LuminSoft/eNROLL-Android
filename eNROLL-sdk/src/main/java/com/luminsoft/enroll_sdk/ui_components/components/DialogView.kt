package com.luminsoft.enroll_sdk.ui_components.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appColors
import com.luminsoft.ekyc_android_sdk.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogView(
    bottomSheetStatus: BottomSheetStatus,
    text: String,
    buttonText: String,
    onPressedButton: () -> Unit,
    secondButtonText: String? = null,
    onPressedSecondButton: (() -> Unit)? = null,
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        modifier = Modifier
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = MaterialTheme.appColors.backGround),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.appColors.backGround)
                .safeContentPadding()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box {
                    Image(
                        painterResource(R.drawable.pop_up_header),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth(),
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Image(
                            painterResource(getImageId(bottomSheetStatus)),
                            contentDescription = "",

                            )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = text,
                    color = MaterialTheme.appColors.appBlack,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        ButtonView(
                            onClick = {
                                onPressedButton()
                            },
                            title = buttonText,
                            textColor = MaterialTheme.appColors.white,
                            color = getColor(bottomSheetStatus = bottomSheetStatus)
                        )
                    }
                    if (secondButtonText != null && onPressedSecondButton != null) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {

                            ButtonView(
                                onClick = {
                                    onPressedSecondButton()
                                },
                                title = secondButtonText,
                                color = MaterialTheme.appColors.backGround,
                                borderColor = getColor(bottomSheetStatus = bottomSheetStatus),
                                textColor = getColor(bottomSheetStatus = bottomSheetStatus)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun getColor(bottomSheetStatus: BottomSheetStatus): Color {
    return when (bottomSheetStatus) {
        BottomSheetStatus.SUCCESS -> {
            MaterialTheme.appColors.successColor
        }

        BottomSheetStatus.WARNING -> {
            MaterialTheme.appColors.warningColor
        }

        BottomSheetStatus.ERROR -> {
            MaterialTheme.appColors.errorColor
        }
    }
}

fun getImageId(bottomSheetStatus: BottomSheetStatus): Int {
    return when (bottomSheetStatus) {
        BottomSheetStatus.SUCCESS -> {
            R.drawable.success_popup_icon
        }

        BottomSheetStatus.WARNING -> {
            R.drawable.alert_popup_icon
        }

        BottomSheetStatus.ERROR -> {
            R.drawable.error_popup_icon
        }
    }
}


enum class BottomSheetStatus {

    SUCCESS, ERROR, WARNING
}