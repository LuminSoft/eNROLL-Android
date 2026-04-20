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
    val machineReadableZone = travelDocument.machineReadableZoneInformation?.machineReadableZone
    val td3 = machineReadableZone?.td3
    val td2 = machineReadableZone?.td2
    val td1 = machineReadableZone?.td1
    val mrzName = td3?.name ?: td2?.name ?: td1?.name
    val fullNameEn = buildFullName(
        primary = mrzName?.primaryIdentifier?.value,
        secondary = mrzName?.secondaryIdentifier?.value,
    ).ifBlank {
        buildFullName(
            primary = travelDocument.additionalPersonalDetails?.nameOfHolder?.primaryIdentifier,
            secondary = travelDocument.additionalPersonalDetails?.nameOfHolder?.secondaryIdentifier,
        )
    }
    val genderValue = td3?.sex?.value ?: td2?.sex?.value ?: td1?.sex?.value
    val birthDateValue = td3?.dateOfBirth?.value ?: td2?.dateOfBirth?.value ?: td1?.dateOfBirth?.value
    val expiryDateValue = td3?.dateOfExpiry?.value ?: td2?.dateOfExpiry?.value ?: td1?.dateOfExpiry?.value
    val documentNumberValue = td3?.passportNumber?.value ?: td2?.documentNumber?.value ?: td1?.documentNumber?.value
    val issuingAuthorityValue = travelDocument.additionalDocumentDetails?.issuingAuthority
    val nationalityValue = td3?.nationality?.value ?: td2?.nationality?.value ?: td1?.nationality?.value
    val documentCodeValue = td3?.documentCode?.value ?: td2?.documentCode?.value ?: td1?.documentCode?.value
    val visualZoneValue = machineReadableZone?.lines?.joinToString("\n").orEmpty()

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

            if (fullNameEn.isNotBlank()) {
                TextItem(
                    label = R.string.nameEn,
                    value = fullNameEn,
                    icon = R.drawable.user_icon
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            birthDateValue?.takeIf { it.isNotBlank() }?.let {
                TextItem(
                    label = R.string.birthDate,
                    value = formatMrzDate(it),
                    icon = R.drawable.calendar_icon
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            documentNumberValue?.takeIf { it.isNotBlank() }?.let {
                TextItem(
                    label = R.string.passportDocumentNumber,
                    value = it,
                    icon = R.drawable.passport_icon
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            expiryDateValue?.takeIf { it.isNotBlank() }?.let {
                TextItem(
                    label = R.string.dateOfExpiry,
                    value = formatMrzDate(it),
                    icon = R.drawable.calendar_icon
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            genderValue?.takeIf { it.isNotBlank() }?.let {
                TextItem(
                    label = R.string.gender,
                    value = it,
                    icon = R.drawable.gender_icon
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            issuingAuthorityValue?.takeIf { it.isNotBlank() }?.let {
                TextItem(
                    label = R.string.issuingAuthority,
                    value = it,
                    icon = R.drawable.issuing_authurity_icon
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            documentCodeValue?.takeIf { it.isNotBlank() }?.let {
                TextItem(
                    label = R.string.documentCode,
                    value = it,
                    icon = R.drawable.factory_num_icon
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            nationalityValue?.takeIf { it.isNotBlank() }?.let {
                TextItem(
                    label = R.string.nationality,
                    value = it,
                    icon = R.drawable.nationality_icon
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (visualZoneValue.isNotBlank()) {
                TextItem(
                    label = R.string.visualZone,
                    value = visualZoneValue,
                    icon = R.drawable.factory_num_icon,
                    height = 120.0
                )
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
private fun TextItem(label: Int, value: String, icon: Int, height: Double = 60.0) {
    val fieldIcon = resolveFieldIcon(icon)
    NormalTextField(
        label = ResourceProvider.instance.getStringResource(label),
        value = TextFieldValue(text = getDisplayValue(label, value)),
        onValueChange = { },
        enabled = false,
        singleLine = false,
        height = height,
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
        "${trimmed.substring(6, 8)}-${trimmed.substring(4, 6)}-${trimmed.substring(0, 4)}"
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
        "$dd-$mm-$century${trimmed.substring(0, 2)}"
    } catch (_: Exception) {
        raw
    }
}

private fun buildFullName(primary: String?, secondary: String?): String {
    return listOf(primary, secondary)
        .mapNotNull { it?.replace('<', ' ')?.trim()?.replace(Regex("\\s+"), " ") }
        .filter { it.isNotBlank() }
        .joinToString(" ")
}

private fun getDisplayValue(label: Int, value: String): String {
    return if (label == R.string.gender) {
        when (value.trim().uppercase()) {
            "M" -> ResourceProvider.instance.getStringResource(R.string.male)
            "F" -> ResourceProvider.instance.getStringResource(R.string.female)
            else -> value
        }
    } else {
        value
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
