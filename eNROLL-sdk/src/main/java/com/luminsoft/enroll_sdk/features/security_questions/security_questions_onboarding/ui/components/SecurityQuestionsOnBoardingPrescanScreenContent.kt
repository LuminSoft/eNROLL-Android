package com.luminsoft.enroll_sdk.features.security_questions.security_questions_onboarding.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.ColorFilter
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
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.GetSecurityQuestionsResponseModel
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_domain.usecases.GetSecurityQuestionsUseCase
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_domain.usecases.PostSecurityQuestionsUseCase
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_navigation.securityQuestionsOnBoardingScreenContent
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_onboarding.view_model.SecurityQuestionsOnBoardingViewModel
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.EkycStepType
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import org.koin.compose.koinInject


var answerValidate = mutableStateOf(false)

@Composable
fun SecurityQuestionsOnBoardingScreenContent(
    onBoardingViewModel: OnBoardingViewModel,
    navController: NavController,
) {
    val getSecurityQuestionsUseCase =
        GetSecurityQuestionsUseCase(koinInject())

    val postSecurityQuestionsUseCase =
        PostSecurityQuestionsUseCase(koinInject())

    val securityQuestionsViewModel =
        remember {
            SecurityQuestionsOnBoardingViewModel(
                getSecurityQuestionsUseCase = getSecurityQuestionsUseCase,
                onBoardingViewModel = onBoardingViewModel,
                postSecurityQuestionsUseCase = postSecurityQuestionsUseCase
            )
        }

    val securityQuestionsOnBoardingVM = remember { securityQuestionsViewModel }

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = securityQuestionsViewModel.loading.collectAsState()
    val securityQuestionsApproved =
        securityQuestionsViewModel.securityQuestionsApproved.collectAsState()
    val failure = securityQuestionsViewModel.failure.collectAsState()
    val selectedQuestion = securityQuestionsViewModel.selectedQuestion.collectAsState()
    val answer = securityQuestionsViewModel.answer.collectAsState()
    val selectedSecurityQuestions =
        onBoardingViewModel.selectedSecurityQuestions.collectAsState()
    val securityQuestions = onBoardingViewModel.securityQuestionsList.collectAsState()
    val securityQuestionsAPI = onBoardingViewModel.securityQuestions.collectAsState()
    val selectQuestionError = securityQuestionsViewModel.selectQuestionError.collectAsState()
    val answerError = securityQuestionsViewModel.answerError.collectAsState()



    BackGroundView(navController = navController, showAppBar = true) {
        if (securityQuestionsApproved.value) {
            val isEmpty =
                onBoardingViewModel.removeCurrentStep(EkycStepType.SecurityQuestions.getStepId())
            if (isEmpty)
                DialogView(
                    bottomSheetStatus = BottomSheetStatus.SUCCESS,
                    text = stringResource(id = R.string.successfulRegistration),
                    buttonText = stringResource(id = R.string.continue_to_next),
                    onPressedButton = {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(
                            EnrollFailedModel(
                                activity.getString(R.string.successfulRegistration),
                                activity.getString(R.string.successfulRegistration)
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
                            securityQuestionsOnBoardingVM.getSecurityQuestions()
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

            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                Image(
                    painterResource(R.drawable.step_06_security_questions),
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight,
                    colorFilter =   ColorFilter.tint(MaterialTheme.appColors.primary),

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
                    color = Color.Black
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                DropdownList(
                    securityQuestions.value,
                    securityQuestionsViewModel,
                    selectedQuestion,
                    onBoardingViewModel,
                    selectQuestionError
                )

                Spacer(modifier = Modifier.height(20.dp))
                AnswerTextField(answer, securityQuestionsOnBoardingVM, answerError)
                Spacer(modifier = Modifier.fillMaxHeight(0.4f))

                ButtonView(
                    onClick = {
                        answerValidate.value = true
                        securityQuestionsOnBoardingVM.onChangeValue(securityQuestionsOnBoardingVM.answer.value)
                        if (selectedQuestion.value != null) {
                            val securityQuestionModel = GetSecurityQuestionsResponseModel()
                            securityQuestionModel.question = selectedQuestion.value!!.question
                            securityQuestionModel.id = selectedQuestion.value!!.id
                            securityQuestionModel.answer = answer.value.text

                            onBoardingViewModel.selectedSecurityQuestions.value.add(
                                securityQuestionModel
                            )
                            onBoardingViewModel.securityQuestionsList.value.remove(selectedQuestion.value!!)

                            if (selectedSecurityQuestions.value.size < 3)
                                navController.navigate(securityQuestionsOnBoardingScreenContent)
                            else
                                securityQuestionsOnBoardingVM.postSecurityQuestionsCall()
                        } else
                            securityQuestionsViewModel.selectQuestionError.value = true
                    },
                    title = stringResource(id = R.string.confirmAndContinue)
                )

                Spacer(modifier = Modifier.height(20.dp))

            }
        }
    }
}

@Composable
private fun AnswerTextField(
    answer: State<TextFieldValue>,
    securityQuestionsOnBoardingVM: SecurityQuestionsOnBoardingViewModel,
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
                    colorFilter =   ColorFilter.tint(MaterialTheme.appColors.primary),
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
    value: List<GetSecurityQuestionsResponseModel>,
    securityQuestionsViewModel: SecurityQuestionsOnBoardingViewModel,
    selectedQuestion: State<GetSecurityQuestionsResponseModel?>,
    onBoardingViewModel: OnBoardingViewModel,
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
                    onBoardingViewModel.securityQuestions.value?.first { it.question == it1 }
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
                    colorFilter =   ColorFilter.tint(MaterialTheme.appColors.primary),
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
                                onBoardingViewModel.securityQuestions.value?.first { it == label }
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
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    disabledContainerColor = Color.White,
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    disabledTextColor = Color.Black,
    focusedIndicatorColor = MaterialTheme.appColors.primary,
    unfocusedIndicatorColor = MaterialTheme.appColors.primary,
    disabledIndicatorColor = MaterialTheme.appColors.primary,
)

@Composable
fun StepsProgressBar(modifier: Modifier = Modifier, numberOfSteps: Int, currentStep: Int) {
    Row(
        modifier = modifier,
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
        if (isCompete || isCurrent) MaterialTheme.appColors.primary else Color(0xffEBEBEB)

    Box(modifier = modifier) {

        //Line
        if (!isFirstItem)
            Divider(
                modifier = Modifier.align(Alignment.CenterStart),
                color = color,
                thickness = 2.dp
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



