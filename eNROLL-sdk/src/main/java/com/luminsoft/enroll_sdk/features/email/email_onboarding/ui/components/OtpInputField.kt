package com.luminsoft.enroll_sdk.features.email.email_onboarding.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


private data class OtpField(
    val text: String,
    val index: Int,
    val focusRequester: FocusRequester? = null
)

@Composable
fun OtpInputField(
    otp: MutableState<String>, // The current OTP value.
    count: Int = 6, // Number of OTP boxes.
    otpTextType: KeyboardType = KeyboardType.Number,
    textColor: Color = MaterialTheme.colorScheme.primary,
) {

    val scope = rememberCoroutineScope()

    val otpFieldsValues = remember {
        (0 until count).mapIndexed { index, i ->
            mutableStateOf(
                OtpField(
                    text = otp.value.getOrNull(i)?.toString() ?: "",
                    index = index,
                    focusRequester = FocusRequester()
                )
            )
        }
    }

    LaunchedEffect(key1 = otp.value) {
        for (i in otpFieldsValues.indices) {
            otpFieldsValues[i].value =
                otpFieldsValues[i].value.copy(text = otp.value.getOrNull(i)?.toString() ?: "")
        }
        // Request focus on the first box if the OTP is blank (e.g., reset).
        if (otp.value.isBlank()) {
            try {
                otpFieldsValues[0].value.focusRequester?.requestFocus()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Create a row of OTP boxes.
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(count) { index ->
            // For each OTP box, manage its value, focus, and what happens on value change.
            OtpBox(
                modifier = Modifier
                    .bottomStroke(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 5.pxToDp()
                    ),
                otpValue = otpFieldsValues[index].value,
                textType = otpTextType,
                textColor = textColor,
                isLastItem = index == count - 1, // Check if this box is the last in the sequence.
                totalBoxCount = count,
                onValueChange = { newValue ->
                    // Handling logic for input changes, including moving focus and updating OTP state.
                    scope.launch {
                        handleOtpInputChange(index, count, newValue, otpFieldsValues, otp)
                    }
                },
                onFocusSet = { focusRequester ->
                    // Save the focus requester for each box to manage focus programmatically.
                    otpFieldsValues[index].value =
                        otpFieldsValues[index].value.copy(focusRequester = focusRequester)
                },
                onNext = {
                    // Attempt to move focus to the next box when the "next" action is triggered.
                    focusNextBox(index, count, otpFieldsValues)
                },
            )
            Spacer(modifier = Modifier.width(5.dp))

        }
    }
}

private fun handleOtpInputChange(
    index: Int,
    count: Int,
    newValue: String,
    otpFieldsValues: List<MutableState<OtpField>>,
    otp: MutableState<String>
) {
    // Handle input for the current box.
    if (newValue.length <= 1) {
        otpFieldsValues[index].value = otpFieldsValues[index].value.copy(text = newValue)
    } else if (newValue.length == 2) {

        otpFieldsValues[index].value =
            otpFieldsValues[index].value.copy(text = newValue.lastOrNull()?.toString() ?: "")
    } else if (newValue.isNotEmpty()) {
        newValue.forEachIndexed { i, char ->
            if (index + i < count) {
                otpFieldsValues[index + i].value =
                    otpFieldsValues[index + i].value.copy(text = char.toString())
            }
        }
    }

    // Update the overall OTP state.
    var currentOtp = ""
    otpFieldsValues.forEach {
        currentOtp += it.value.text
    }

    try {
        if (newValue.isEmpty() && index > 0) {
            otpFieldsValues.getOrNull(index - 1)?.value?.focusRequester?.requestFocus()
        } else if (index < count - 1 && newValue.isNotEmpty()) {
            otpFieldsValues.getOrNull(index + 1)?.value?.focusRequester?.requestFocus()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    otp.value = currentOtp
}

private fun focusNextBox(
    index: Int,
    count: Int,
    otpFieldsValues: List<MutableState<OtpField>>
) {
    if (index + 1 < count) {
        try {
            otpFieldsValues[index + 1].value.focusRequester?.requestFocus()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


@Composable
private fun OtpBox(
    modifier: Modifier,
    otpValue: OtpField, // Current value of this OTP box.
    textType: KeyboardType = KeyboardType.Number,
    textColor: Color = MaterialTheme.colorScheme.primary,
    isLastItem: Boolean, // Whether this box is the last in the sequence.
    totalBoxCount: Int, // Total number of OTP boxes for layout calculations.
    onValueChange: (String) -> Unit, // Callback for when the value changes.
    onFocusSet: (FocusRequester) -> Unit, // Callback to set focus requester.
    onNext: () -> Unit, // Callback for handling "next" action, typically moving focus forward.
) {
    val focusManager = LocalFocusManager.current
    val focusRequest = otpValue.focusRequester ?: FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current


    val screenWidth = LocalConfiguration.current.screenWidthDp.dp.dpToPx().toInt()
    val paddingValue = 10
    val totalBoxSize = (screenWidth / totalBoxCount) - paddingValue * totalBoxCount

    Box(
        modifier = modifier
            .size(totalBoxSize.pxToDp()),
        contentAlignment = Alignment.Center,
    ) {
        BasicTextField(
            value = TextFieldValue(otpValue.text, TextRange(maxOf(0, otpValue.text.length))),
            onValueChange = {
                if (!it.text.equals(otpValue)) {
                    onValueChange(it.text)
                }
            },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .testTag("otpBox${otpValue.index}")
                .focusRequester(focusRequest)
                .onGloballyPositioned {
                    onFocusSet(focusRequest)
                },
            textStyle = MaterialTheme.typography.titleLarge.copy(
                textAlign = TextAlign.Center,
                color = textColor
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = textType,
                imeAction = if (isLastItem) ImeAction.Done else ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNext()
                },
                onDone = {
                    // Hide keyboard and clear focus when done.
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            singleLine = true,
            visualTransformation = getVisualTransformation(textType),
        )
    }
}

@Composable
private fun getVisualTransformation(textType: KeyboardType) =
    if (textType == KeyboardType.NumberPassword || textType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }


fun Modifier.bottomStroke(color: Color, strokeWidth: Dp = 2.dp): Modifier = this.then(
    Modifier.drawBehind {
        val strokePx = strokeWidth.toPx()
        // Draw a line at the bottom
        drawLine(
            color = color,
            start = Offset(x = 0f, y = size.height - strokePx / 2),
            end = Offset(x = size.width, y = size.height - strokePx / 2),
            strokeWidth = strokePx
        )
    }
)