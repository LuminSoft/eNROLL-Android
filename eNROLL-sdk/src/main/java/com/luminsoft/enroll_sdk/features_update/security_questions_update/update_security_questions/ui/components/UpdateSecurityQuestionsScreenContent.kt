package com.luminsoft.enroll_sdk.features_update.security_questions_update.update_security_questions.ui.components

import GetSecurityQuestionsUpdateResponseModel
import GetSecurityQuestionsUpdateUseCase
import UpdateSecurityQuestionsUseCase
import UpdateSecurityQuestionsViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import appColors
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.main_update.main_update_navigation.updateListScreenContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import org.koin.compose.koinInject
import updateSecurityQuestionsScreenContent


var updateAnswerValidate = mutableStateOf(false)

@Composable
fun UpdateSecurityQuestionsScreenContent(
    updateViewModel: UpdateViewModel,
    navController: NavController,
) {
    val getSecurityQuestionsUseCase =
        GetSecurityQuestionsUpdateUseCase(koinInject())

    val updateSecurityQuestionsUseCase =
        UpdateSecurityQuestionsUseCase(koinInject())

    val securityQuestionsViewModel =
        remember {
            UpdateSecurityQuestionsViewModel(
                getSecurityQuestionsUseCase = getSecurityQuestionsUseCase,
                updateViewModel = updateViewModel,
                postSecurityQuestionsUseCase = updateSecurityQuestionsUseCase
            )
        }


    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = securityQuestionsViewModel.loading.collectAsState()
    val securityQuestionsApproved =
        securityQuestionsViewModel.securityQuestionsApproved.collectAsState()
    val failure = securityQuestionsViewModel.failure.collectAsState()
    val selectedQuestion = securityQuestionsViewModel.selectedQuestion.collectAsState()
    val answer = securityQuestionsViewModel.answer.collectAsState()
    val selectedSecurityQuestions =
        updateViewModel.selectedSecurityQuestions.collectAsState()
    val securityQuestions = updateViewModel.securityQuestionsList.collectAsState()
    val securityQuestionsAPI = updateViewModel.securityQuestions.collectAsState()
    val selectQuestionError = securityQuestionsViewModel.selectQuestionError.collectAsState()
    val answerError = securityQuestionsViewModel.answerError.collectAsState()



    BackGroundView(navController = navController, showAppBar = true) {
        if (securityQuestionsApproved.value) {
            DialogView(
                bottomSheetStatus = BottomSheetStatus.SUCCESS,
                text = stringResource(id = R.string.successfulUpdate),
                buttonText = stringResource(id = R.string.continue_to_next),
                onPressedButton = {
                    activity.finish()
                    EnrollSDK.enrollCallback?.error(
                        EnrollFailedModel(
                            activity.getString(R.string.successfulUpdate),
                            activity.getString(R.string.successfulUpdate)
                        )
                    )
                },
            )
        }
        if (loading.value) LoadingView()
        else if (!failure.value?.message.isNullOrEmpty()) {
            if (failure.value is AuthFailure) {
                failure.value?.let {
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                        },
                    ) {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                    }
                }
            } else {
                failure.value?.let {
                    DialogView(bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.retry),
                        onPressedButton = {
                            securityQuestionsViewModel.getSecurityQuestions()
                        },
                        secondButtonText = stringResource(id = R.string.exit),
                        onPressedSecondButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                        }) {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                    }
                }
            }
        } else if (!securityQuestionsAPI.value.isNullOrEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
                    .systemBarsPadding()
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                Image(
                    painterResource(R.drawable.step_06_security_questions),
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight(0.2f)
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.07f))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StepsProgressBar(
                        modifier = Modifier
                            .fillMaxWidth(0.4f),
                        numberOfSteps = 2,
                        currentStep = selectedSecurityQuestions.value.size,
                    )
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.07f))

                Text(
                    text = stringResource(id = R.string.youMustChooseThreeQuestions),
                    fontSize = 12.sp,
                    color = MaterialTheme.appColors.textColor,
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                DropdownList(
                    securityQuestions.value,
                    securityQuestionsViewModel,
                    selectedQuestion,
                    updateViewModel,
                    selectQuestionError
                )

                Spacer(modifier = Modifier.height(20.dp))
                AnswerUpdateTextField(answer, securityQuestionsViewModel, answerError)
                Spacer(modifier = Modifier.fillMaxHeight(0.2f))

                ButtonView(
                    onClick = {
                        updateAnswerValidate.value = true
                        securityQuestionsViewModel.onChangeValue(securityQuestionsViewModel.answer.value)

                        val securityQuestionModel = GetSecurityQuestionsUpdateResponseModel()

                        val isAnswerValid =
                            answer.value.text.isNotEmpty() && answer.value.text.length <= 150
                        val isQuestionSelected = selectedQuestion.value != null
                        var selectedQuestionValue: GetSecurityQuestionsUpdateResponseModel? = null

                        if (isAnswerValid) {
                            securityQuestionModel.answer = answer.value.text
                        } else {
                            securityQuestionsViewModel.answerError.value =
                                ResourceProvider.instance.getStringResource(R.string.errorEmptyAnswer)
                        }

                        if (isQuestionSelected) {
                            selectedQuestionValue = selectedQuestion.value!!
                            securityQuestionModel.question = selectedQuestionValue.question
                            securityQuestionModel.id = selectedQuestionValue.id

                        } else {
                            securityQuestionsViewModel.selectQuestionError.value = true
                        }

                        if (isAnswerValid && isQuestionSelected) {

                            updateViewModel.selectedSecurityQuestions.value.add(
                                securityQuestionModel
                            )
                            updateViewModel.securityQuestionsList.value.remove(selectedQuestionValue)

                            if (updateViewModel.selectedSecurityQuestions.value.size < 3) {
                                navController.navigate(updateSecurityQuestionsScreenContent)
                            } else {
                                securityQuestionsViewModel.postSecurityQuestionsCall()
                            }
                        }
                    },
                    title = stringResource(id = R.string.confirmAndContinue)
                )
                Spacer(modifier = Modifier.height(8.dp))

                ButtonView(
                    onClick = {
                        updateViewModel.securityQuestions.value = null
                        updateViewModel.selectedSecurityQuestions.value.clear()
                        navController.navigate(updateListScreenContent)
                    },
                    stringResource(id = R.string.cancel),
                    color = MaterialTheme.appColors.backGround,
                    borderColor = MaterialTheme.appColors.primary,
                    textColor = MaterialTheme.appColors.primary,
                )
                Spacer(modifier = Modifier.height(20.dp))

            }
        }
    }
}

@Composable
private fun AnswerUpdateTextField(
    answer: State<TextFieldValue>,
    securityQuestionsOnBoardingVM: UpdateSecurityQuestionsViewModel,
    answerError: State<String?>
) {
    val maxChar = 150

    Column {
        TextField(
            value = answer.value,
            onValueChange = {
                if (it.text.length <= maxChar)
                    securityQuestionsOnBoardingVM.onChangeValue(it)
            },
            supportingText = {
                Text(
                    text = "${answer.value.text.length} / $maxChar",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(stringResource(id = R.string.answer), fontSize = 12.sp) },
            colors = textFieldColors(),
            leadingIcon = {
                Image(
                    painterResource(R.drawable.answer_icon),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "",
                )
            },
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 12.sp,
                color = Color.Black
            )
        )
        if (answerError.value != null)
            Text(
                answerError.value!!,
                color = MaterialTheme.appColors.errorColor,
                style = MaterialTheme.typography.labelSmall
            )
    }
}

@Composable
fun DropdownList(
    value: List<GetSecurityQuestionsUpdateResponseModel>,
    securityQuestionsViewModel: UpdateSecurityQuestionsViewModel,
    selectedQuestion: State<GetSecurityQuestionsUpdateResponseModel?>,
    updateViewModel: UpdateViewModel,
    selectQuestionError: State<Boolean>,
) {

    var mExpanded by remember { mutableStateOf(false) }
    val selectedText = if (selectedQuestion.value != null) selectedQuestion.value!!.question else ""
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.fillMaxWidth()) {
        TextField(
            value = selectedText!!,
            enabled = false,
            onValueChange = { it1 ->
                securityQuestionsViewModel.selectedQuestion.value =
                    updateViewModel.securityQuestions.value?.first { it.question == it1 }
            },
            modifier = Modifier
                .clickable { mExpanded = !mExpanded }
                .fillMaxWidth(),
            placeholder = {
                Text(
                    stringResource(id = R.string.chooseAQuestions),
                    fontSize = 12.sp
                )
            },
            colors = textFieldColors(),
            trailingIcon = {
                Icon(
                    icon, "contentDescription",
                    Modifier.clickable { mExpanded = !mExpanded },
                    tint = MaterialTheme.appColors.primary
                )
            },
            leadingIcon = {
                Image(
                    painterResource(R.drawable.info_icon),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "",
                )
            },
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 12.sp,
                color = Color.Black
            )
        )
        if (selectQuestionError.value)
            Text(
                text = stringResource(id = R.string.required_question),
                color = MaterialTheme.appColors.errorColor,
                style = MaterialTheme.typography.labelSmall
            )

        MaterialTheme {
            DropdownMenu(
                expanded = mExpanded,
                onDismissRequest = { mExpanded = false },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White)
            ) {
                value.forEach { label ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = label.question!!,
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        },
                        onClick = {
                            securityQuestionsViewModel.selectedQuestion.value =
                                updateViewModel.securityQuestions.value?.first { it == label }
                            mExpanded = false
                            securityQuestionsViewModel.selectQuestionError.value = false
                        })
                }
            }
        }
    }
}

@Composable
private fun textFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.appColors.white,
    unfocusedContainerColor = MaterialTheme.appColors.white,
    disabledContainerColor = MaterialTheme.appColors.white,
    focusedTextColor = MaterialTheme.appColors.appBlack,
    unfocusedTextColor = MaterialTheme.appColors.appBlack,
    disabledTextColor = MaterialTheme.appColors.appBlack,
    focusedIndicatorColor = MaterialTheme.appColors.primary,
    unfocusedIndicatorColor = MaterialTheme.appColors.primary,
    disabledIndicatorColor = MaterialTheme.appColors.primary,
)

@Composable
fun StepsProgressBar(modifier: Modifier = Modifier, numberOfSteps: Int, currentStep: Int) {
    Row(
        modifier = modifier.fillMaxWidth(), // Ensure full width of the Row is used
        horizontalArrangement = Arrangement.Center, // Center items within the Row
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (step in 0..numberOfSteps) {
            Step(
                modifier = Modifier.weight(1F),
                isCompete = step < currentStep,
                isCurrent = step == currentStep,
                isFirstItem = step == 0
            )
        }
    }
}

@Composable
fun Step(
    modifier: Modifier = Modifier,
    isCompete: Boolean,
    isCurrent: Boolean,
    isFirstItem: Boolean
) {
    val color =
        if (isCompete || isCurrent) MaterialTheme.appColors.primary else MaterialTheme.appColors.secondary

    Box(modifier = modifier) {

        //Line
        if (!isFirstItem)
            HorizontalDivider(
                modifier = Modifier.align(Alignment.CenterStart),
                thickness = 2.dp,
                color = color
            )

        //Circle
        Canvas(modifier = Modifier
            .size(15.dp)
            .align(Alignment.CenterEnd)
            .border(
                shape = CircleShape,
                width = 2.dp,
                color = color
            ),
            onDraw = {
                drawCircle(color = color)
            }
        )
    }
}



