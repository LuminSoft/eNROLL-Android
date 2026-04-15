package com.luminsoft.enroll_sdk.innovitices.nfcreading.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.innovitices.nfcreading.NfcReadingResult
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.NormalTextField
import com.luminsoft.enroll_sdk.ui_components.components.SpinKitLoadingIndicator
import com.luminsoft.enroll_sdk.ui_components.theme.appColors
import com.luminsoft.enroll_sdk.ui_components.theme.resolveFieldIcon
import com.luminsoft.enroll_sdk.ui_components.theme.resolvedPainter
import com.luminsoft.enroll_sdk.ui_components.theme.IconRenderingMode

@Composable
fun NfcResultScreen(
    result: NfcReadingResult,
    isUploading: Boolean = false,
    uploadFailure: SdkFailure? = null,
    onConfirmUpload: () -> Unit,
    onResetFailure: () -> Unit,
    onClose: () -> Unit,
    onErrorAcknowledged: (String) -> Unit,
) {
    val travelDocument = result.nfcTravelDocumentReaderResult.travelDocument

    // Show full-screen loading during upload
    if (isUploading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.appColors.backGround),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpinKitLoadingIndicator()
        }
        return
    }

    // Show error dialog for upload failure - Done exits the entire ePassport flow
    uploadFailure?.let { failure ->
        val failureMessage = failure.message ?: stringResource(id = R.string.someThingWentWrong)
        DialogView(
            bottomSheetStatus = BottomSheetStatus.ERROR,
            text = failureMessage,
            buttonText = stringResource(id = R.string.done),
            onPressedButton = {
                onResetFailure()
                onErrorAcknowledged(failureMessage)
            },
            onDismiss = {
                onResetFailure()
                onErrorAcknowledged(failureMessage)
            }
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.appColors.backGround)
            .padding(horizontal = 24.dp)
    ) {
        // Scrollable content area
        Column(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Face image if available
            result.faceBitmap?.let { bitmap ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FaceImageView(bitmap = bitmap)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // Personal details from NFC chip
            travelDocument.additionalPersonalDetails?.let { details ->
                details.nameOfHolder?.let { name ->
                    name.primaryIdentifier?.let {
                        TextItem(
                            label = R.string.nfc_surname,
                            value = it,
                            icon = R.drawable.user_icon
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    name.secondaryIdentifier?.let {
                        TextItem(
                            label = R.string.nfc_given_names,
                            value = it,
                            icon = R.drawable.user_icon
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                details.fullDateOfBirth?.let {
                    TextItem(
                        label = R.string.birthDate,
                        value = formatChipDate(it),
                        icon = R.drawable.calendar_icon
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                details.placeOfBirth?.let { pob ->
                    if (pob.isNotEmpty()) {
                        TextItem(
                            label = R.string.nfc_place_of_birth,
                            value = pob.joinToString(", "),
                            icon = R.drawable.address_icon
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            // Document details
            travelDocument.additionalDocumentDetails?.let { details ->
                details.dateOfIssue?.let {
                    TextItem(
                        label = R.string.nfc_date_of_issue,
                        value = formatChipDate(it),
                        icon = R.drawable.calendar_icon
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                details.issuingAuthority?.let {
                    TextItem(
                        label = R.string.issuingAuthority,
                        value = it,
                        icon = R.drawable.issuing_authurity_icon
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // MRZ-based document fields (TD3)
            travelDocument.machineReadableZoneInformation?.machineReadableZone?.td3?.let { td3 ->
                td3.passportNumber?.value?.let { passportNum ->
                    if (passportNum.isNotBlank()) {
                        TextItem(
                            label = R.string.passportDocumentNumber,
                            value = passportNum,
                            icon = R.drawable.issuing_authurity_icon
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                td3.sex?.value?.let { sexValue ->
                    if (sexValue.isNotBlank()) {
                        TextItem(
                            label = R.string.sex,
                            value = sexValue,
                            icon = R.drawable.user_icon
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                td3.dateOfExpiry?.value?.let { expiryDate ->
                    if (expiryDate.isNotBlank()) {
                        TextItem(
                            label = R.string.dateOfExpiry,
                            value = formatMrzDate(expiryDate),
                            icon = R.drawable.calendar_icon
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                td3.nationality?.value?.let { nat ->
                    if (nat.isNotBlank()) {
                        TextItem(
                            label = R.string.nationality,
                            value = nat,
                            icon = R.drawable.issuing_authurity_icon
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            // Additional personal details
            travelDocument.additionalPersonalDetails?.let { details ->
                details.otherNames?.let { names ->
                    if (names.isNotEmpty()) {
                        TextItem(
                            label = R.string.nfc_other_names,
                            value = names.joinToString(", "),
                            icon = R.drawable.user_icon
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                details.address?.let { addressList ->
                    if (addressList.isNotEmpty()) {
                        TextItem(
                            label = R.string.nfc_address,
                            value = addressList.joinToString(", "),
                            icon = R.drawable.address_icon
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bottom buttons
        Column {
            ButtonView(
                onClick = onConfirmUpload,
                title = stringResource(id = R.string.confirmAndContinue)
            )
            Spacer(modifier = Modifier.height(8.dp))

            ButtonView(
                onClick = onClose,
                title = stringResource(id = R.string.cancel),
                color = MaterialTheme.appColors.backGround,
                borderColor = MaterialTheme.appColors.primary,
                textColor = MaterialTheme.appColors.primary
            )
        }
    }
}

@Composable
private fun TextItem(label: Int, value: String, icon: Int) {
    val fieldIcon = resolveFieldIcon(icon)
    NormalTextField(
        label = ResourceProvider.instance.getStringResource(label),
        value = TextFieldValue(text = value),
        onValueChange = { },
        enabled = false,
        height = 60.0,
        icon = {
            Image(
                resolvedPainter(fieldIcon, icon),
                contentDescription = "",
                colorFilter = if (fieldIcon?.renderingMode == IconRenderingMode.ORIGINAL) null
                    else ColorFilter.tint(MaterialTheme.appColors.primary),
                modifier = Modifier.height(50.dp)
            )
        }
    )
}

/**
 * Formats a chip date from yyyyMMdd to dd/MM/yyyy for display.
 * Returns the original string if it doesn't match the expected format.
 */
private fun formatChipDate(raw: String): String {
    val trimmed = raw.trim()
    if (trimmed.length != 8 || !trimmed.all { it.isDigit() }) return raw
    return try {
        "${trimmed.substring(6, 8)}/${trimmed.substring(4, 6)}/${trimmed.substring(0, 4)}"
    } catch (_: Exception) {
        raw
    }
}

/**
 * Formats an MRZ date from yyMMdd to dd/MM/yyyy for display.
 * Uses a 10-year future window to determine the century.
 */
private fun formatMrzDate(raw: String): String {
    val trimmed = raw.trim()
    if (trimmed.length != 6 || !trimmed.all { it.isDigit() }) return formatChipDate(raw)
    return try {
        val yy = trimmed.substring(0, 2).toInt()
        val mm = trimmed.substring(2, 4)
        val dd = trimmed.substring(4, 6)
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) % 100
        val century = if (yy <= currentYear + 10) "20" else "19"
        "$dd/$mm/$century${trimmed.substring(0, 2)}"
    } catch (_: Exception) {
        raw
    }
}

@Composable
private fun FaceImageView(bitmap: Bitmap) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(60.dp))
            .border(3.dp, MaterialTheme.appColors.primary, RoundedCornerShape(60.dp))
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Face Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
