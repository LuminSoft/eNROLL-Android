package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_presentation.ui.components

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.models.EnrollSuccessModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireQuestionModel
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_presentation.view_model.QuestionnaireViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.EnrollText
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import com.luminsoft.enroll_sdk.ui_components.theme.EnrollTextStyle
import com.luminsoft.enroll_sdk.ui_components.theme.appColors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun QuestionnaireScreenContent(
    viewModel: QuestionnaireViewModel,
    onExit: () -> Unit
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = viewModel.loading.collectAsState()
    val buttonLoading = viewModel.isButtonLoading.collectAsState()
    val failure = viewModel.failure.collectAsState()
    val questions = viewModel.questions.collectAsState()
    val answers = viewModel.answers.collectAsState()
    val fileNames = viewModel.fileNames.collectAsState()
    val validationMessage = viewModel.validationMessage.collectAsState()
    val submittedSuccessfully = viewModel.submittedSuccessfully.collectAsState()
    val navController = rememberNavController()

    LaunchedEffect(submittedSuccessfully.value) {
        if (submittedSuccessfully.value) {
            EnrollSDK.enrollCallback?.success(
                EnrollSuccessModel(
                    enrollMessage = context.getString(R.string.questionnaire_completed_successfully),
                    applicantId = EnrollSDK.applicantId,
                    requestId = viewModel.requestId.value
                )
            )
        }
    }

    BackGroundView(navController = navController, showAppBar = true, onClick = onExit) {
        when {
            loading.value -> LoadingView()
            submittedSuccessfully.value -> {
                DialogView(
                    bottomSheetStatus = BottomSheetStatus.SUCCESS,
                    text = stringResource(id = R.string.questionnaire_completed_successfully),
                    buttonText = stringResource(id = R.string.exit),
                    onPressedButton = onExit,
                    onDismiss = onExit
                )
            }

            !failure.value?.message.isNullOrEmpty() -> {
                failure.value?.let {
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = if (it is AuthFailure) {
                            stringResource(id = R.string.exit)
                        } else {
                            stringResource(id = R.string.retry)
                        },
                        onPressedButton = {
                            if (it is AuthFailure) {
                                EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                                activity.finish()
                            } else {
                                viewModel.retry()
                            }
                        },
                        secondButtonText = if (it is AuthFailure) null else stringResource(id = R.string.exit),
                        onPressedSecondButton = if (it is AuthFailure) null else ({
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                            activity.finish()
                        }),
                        onDismiss = {
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                            activity.finish()
                        }
                    )
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    EnrollText(
                        text = stringResource(id = R.string.questionnaire),
                        color = MaterialTheme.appColors.textColor,
                        style = EnrollTextStyle.TITLE
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    questions.value.forEach { question ->
                        QuestionnaireQuestionItem(
                            question = question,
                            answer = question.id?.let { answers.value[it] },
                            fileName = question.id?.let { fileNames.value[it] },
                            onAnswerChanged = { value ->
                                question.id?.let { viewModel.setAnswer(it, value) }
                            },
                            onFileSelected = { name, base64 ->
                                question.id?.let { viewModel.setFileAnswer(it, name, base64) }
                            },
                            onFileError = viewModel::setFileError
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                    }
                    validationMessage.value?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.appColors.errorColor,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    ButtonView(
                        onClick = viewModel::submit,
                        title = if (buttonLoading.value) {
                            stringResource(id = R.string.loading)
                        } else {
                            stringResource(id = R.string.questionnaire_submit)
                        },
                        isEnabled = !buttonLoading.value
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun QuestionnaireQuestionItem(
    question: QuestionnaireQuestionModel,
    answer: Any?,
    fileName: String?,
    onAnswerChanged: (Any?) -> Unit,
    onFileSelected: (String, String) -> Unit,
    onFileError: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.appColors.primary.copy(alpha = 0.18f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = question.title.orEmpty(),
                color = MaterialTheme.appColors.textColor,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            if (question.isRequired == true) {
                Text(text = "*", color = MaterialTheme.appColors.errorColor)
            }
        }
        question.description?.takeIf { it.isNotBlank() }?.let {
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = it, color = MaterialTheme.appColors.appBlack)
        }
        Spacer(modifier = Modifier.height(10.dp))

        when (question.questionType) {
            QuestionnaireViewModel.QUESTION_TYPE_TEXT -> {
                OutlinedTextField(
                    value = answer as? String ?: "",
                    onValueChange = onAnswerChanged,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text(stringResource(id = R.string.questionnaire_text_placeholder)) }
                )
            }

            QuestionnaireViewModel.QUESTION_TYPE_NUMBER -> {
                OutlinedTextField(
                    value = answer?.toString().orEmpty(),
                    onValueChange = { value ->
                        if (value.isBlank() || value.toDoubleOrNull() != null) {
                            onAnswerChanged(value)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text(stringResource(id = R.string.questionnaire_number_placeholder)) }
                )
            }

            QuestionnaireViewModel.QUESTION_TYPE_BOOLEAN -> {
                BooleanQuestion(answer = answer as? String, onAnswerChanged = onAnswerChanged)
            }

            QuestionnaireViewModel.QUESTION_TYPE_DATE -> {
                DateQuestion(answer = answer as? String, onAnswerChanged = onAnswerChanged)
            }

            QuestionnaireViewModel.QUESTION_TYPE_SINGLE_SELECT -> {
                SingleSelectQuestion(question = question, answer = answer as? String, onAnswerChanged = onAnswerChanged)
            }

            QuestionnaireViewModel.QUESTION_TYPE_MULTI_SELECT -> {
                MultiSelectQuestion(
                    question = question,
                    answer = (answer as? List<*>)?.mapNotNull { it as? Int } ?: emptyList(),
                    onAnswerChanged = onAnswerChanged
                )
            }

            QuestionnaireViewModel.QUESTION_TYPE_UPLOAD_FILE -> {
                UploadFileQuestion(
                    fileName = fileName,
                    onFileSelected = onFileSelected,
                    onFileError = onFileError
                )
            }

            else -> {
                Text(
                    text = stringResource(id = R.string.questionnaire_unsupported_type),
                    color = MaterialTheme.appColors.errorColor
                )
            }
        }
    }
}

@Composable
private fun BooleanQuestion(
    answer: String?,
    onAnswerChanged: (Any?) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
        BooleanOption(
            label = stringResource(id = R.string.yes),
            selected = answer == "true",
            onClick = { onAnswerChanged("true") }
        )
        BooleanOption(
            label = stringResource(id = R.string.no),
            selected = answer == "false",
            onClick = { onAnswerChanged("false") }
        )
    }
}

@Composable
private fun BooleanOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text = label, color = MaterialTheme.appColors.textColor)
    }
}

@Composable
private fun SingleSelectQuestion(
    question: QuestionnaireQuestionModel,
    answer: String?,
    onAnswerChanged: (Any?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedOption = question.questionOptions.firstOrNull { it.id?.toString() == answer }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.appColors.primary.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedOption?.answer ?: stringResource(id = R.string.questionnaire_single_select_placeholder),
                color = MaterialTheme.appColors.textColor,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            question.questionOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.answer.orEmpty()) },
                    onClick = {
                        onAnswerChanged(option.id?.toString().orEmpty())
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun MultiSelectQuestion(
    question: QuestionnaireQuestionModel,
    answer: List<Int>,
    onAnswerChanged: (Any?) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        question.questionOptions.forEach { option ->
            val optionId = option.id
            val selected = optionId != null && answer.contains(optionId)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (optionId != null) {
                            onAnswerChanged(
                                if (selected) answer - optionId else answer + optionId
                            )
                        }
                    }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selected,
                    onCheckedChange = {
                        if (optionId != null) {
                            onAnswerChanged(
                                if (selected) answer - optionId else answer + optionId
                            )
                        }
                    }
                )
                Text(text = option.answer.orEmpty(), color = MaterialTheme.appColors.textColor)
            }
        }
    }
}

@Composable
private fun DateQuestion(
    answer: String?,
    onAnswerChanged: (Any?) -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val displayText = answer?.takeIf { it.length >= 10 }?.take(10).orEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.appColors.primary.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
            .clickable {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        calendar.set(year, month, dayOfMonth, 0, 0, 0)
                        calendar.set(Calendar.MILLISECOND, 0)
                        onAnswerChanged(formatIsoDate(calendar))
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = displayText.ifBlank { stringResource(id = R.string.questionnaire_date_placeholder) },
            color = MaterialTheme.appColors.textColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun UploadFileQuestion(
    fileName: String?,
    onFileSelected: (String, String) -> Unit,
    onFileError: (String) -> Unit
) {
    val context = LocalContext.current
    val invalidExtensionMessage = stringResource(id = R.string.questionnaire_invalid_extension)
    val fileSizeMessage = stringResource(id = R.string.questionnaire_file_size_exceeded)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        val validation = readQuestionnaireFile(context, uri)
        when (validation) {
            is FileReadResult.Success -> onFileSelected(validation.fileName, validation.base64)
            FileReadResult.InvalidExtension -> onFileError(invalidExtensionMessage)
            FileReadResult.FileTooLarge -> onFileError(fileSizeMessage)
            FileReadResult.ReadError -> onFileError(invalidExtensionMessage)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.appColors.primary.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
            .clickable {
                launcher.launch(arrayOf("application/pdf", "image/jpeg", "image/png"))
            }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = fileName ?: stringResource(id = R.string.questionnaire_file_placeholder),
            color = MaterialTheme.appColors.textColor,
            modifier = Modifier.weight(1f)
        )
        if (fileName != null) {
            Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.appColors.successColor)
        }
    }
}

private fun readQuestionnaireFile(context: Context, uri: Uri): FileReadResult {
    return try {
        val fileName = resolveDisplayName(context, uri)
        val extension = fileName.substringAfterLast('.', "").lowercase(Locale.US)
        val mimeExtension = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(context.contentResolver.getType(uri))
            ?.lowercase(Locale.US)
        val allowedExtensions = setOf("pdf", "jpg", "jpeg", "png")
        if (extension !in allowedExtensions && mimeExtension !in allowedExtensions) {
            return FileReadResult.InvalidExtension
        }

        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: return FileReadResult.ReadError
        if (bytes.size > MAX_FILE_SIZE_BYTES) {
            return FileReadResult.FileTooLarge
        }

        FileReadResult.Success(
            fileName = fileName,
            base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
        )
    } catch (e: Exception) {
        FileReadResult.ReadError
    }
}

private fun resolveDisplayName(context: Context, uri: Uri): String {
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex >= 0 && cursor.moveToFirst()) {
            return cursor.getString(nameIndex)
        }
    }
    return uri.lastPathSegment ?: "questionnaire_file"
}

private fun formatIsoDate(calendar: Calendar): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(calendar.time)
}

private sealed class FileReadResult {
    data class Success(val fileName: String, val base64: String) : FileReadResult()
    data object InvalidExtension : FileReadResult()
    data object FileTooLarge : FileReadResult()
    data object ReadError : FileReadResult()
}

private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024
