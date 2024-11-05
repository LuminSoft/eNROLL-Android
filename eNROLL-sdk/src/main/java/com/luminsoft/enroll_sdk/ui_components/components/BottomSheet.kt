package com.luminsoft.enroll_sdk.ui_components.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.luminsoft.enroll_sdk.ui_components.theme.appColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    bottomSheetStatus: BottomSheetStatus,
    text: String,
    buttonText: String,
    onPressedButton: () -> Unit,
    secondButtonText: String? = null,
    onPressedSecondButton: (() -> Unit)? = null,
    onDismiss: () -> Unit = {},
) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(confirmValueChange = { false }, skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },

        sheetState = modalBottomSheetState,
        dragHandle = { },
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.appColors.backGround).safeContentPadding()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painterResource(getImageId(bottomSheetStatus)),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = text,
                    color = MaterialTheme.appColors.appBlack,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row (modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                    Box (modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)){
                        ButtonView(
                            onClick = {
                                onPressedButton()
                            },
                            title = buttonText,
                            color = getColor(bottomSheetStatus = bottomSheetStatus)
                        )
                    }
                    if(secondButtonText != null && onPressedSecondButton != null){
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(modifier =Modifier.fillMaxWidth().weight(1f)){

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

/*
fun getColor(bottomSheetStatus: BottomSheetStatus): Color {
    return when (bottomSheetStatus) {
        BottomSheetStatus.SUCCESS -> {
            successColor
        }

        BottomSheetStatus.WARNING -> {
            warningColor
        }

        BottomSheetStatus.ERROR -> {
            errorColor
        }
    }
}

fun getImageId(bottomSheetStatus: BottomSheetStatus): Int {
    return when (bottomSheetStatus) {
        BottomSheetStatus.SUCCESS -> {
            R.drawable.success_sign
        }

        BottomSheetStatus.WARNING -> {
            R.drawable.warning_sign
        }

        BottomSheetStatus.ERROR -> {
            R.drawable.error_sign
        }
    }
}
*/


/*
enum class BottomSheetStatus {

    SUCCESS, ERROR , WARNING
}*/
