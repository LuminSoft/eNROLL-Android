package com.luminsoft.enroll_sdk.features_auth.security_question_auth.security_question_auth.ui.components

import SecurityQuestionAuthViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.luminsoft.enroll_sdk.ui_components.theme.appColors
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.models.EnrollSuccessModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.widgets.ImagesBox
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features_auth.security_question_auth.security_question_auth_domain.usecases.GetSecurityQuestionAuthUseCase
import com.luminsoft.enroll_sdk.features_auth.security_question_auth.security_question_auth_domain.usecases.ValidateSecurityQuestionUseCase
import com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_models.get_auth_configurations.EkycStepAuthType
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import org.koin.compose.koinInject


var answerValidate = mutableStateOf(false)

@Composable
fun SecurityQuestionAuthScreenContent(
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    val getSecurityQuestionAuthUseCase =
        GetSecurityQuestionAuthUseCase(koinInject())

    val validateSecurityQuestionUseCase =
        ValidateSecurityQuestionUseCase(koinInject())

    val securityQuestionViewModel =
        remember {
            SecurityQuestionAuthViewModel(
                getSecurityQuestionAuthUseCase = getSecurityQuestionAuthUseCase,
                validateSecurityQuestionUseCase = validateSecurityQuestionUseCase,
            )
        }

    val securityQuestionAuthVM = remember { securityQuestionViewModel }

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = securityQuestionAuthVM.loading.collectAsState()
    val securityQuestionApproved = securityQuestionAuthVM.securityQuestionApproved.collectAsState()
    val failure = securityQuestionAuthVM.failure.collectAsState()
    val answer = securityQuestionAuthVM.answer.collectAsState()
    val securityQuestionAPI = securityQuestionAuthVM.securityQuestion.collectAsState()
    val answerError = securityQuestionAuthVM.answerError.collectAsState()



    BackGroundView(navController = navController, showAppBar = true) {
        if (securityQuestionApproved.value) {
            val isEmpty =
                authViewModel.removeCurrentStep(EkycStepAuthType.SecurityQuestion.getStepId())
            if (isEmpty)
                DialogView(
                    bottomSheetStatus = BottomSheetStatus.SUCCESS,
                    text = stringResource(id = R.string.successfulAuthentication),
                    buttonText = stringResource(id = R.string.continue_to_next),
                    onPressedButton = {
                        activity.finish()
                        EnrollSDK.enrollCallback?.success(
                            EnrollSuccessModel(
                                activity.getString(R.string.successfulAuthentication)
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
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                        },
                    )
                }
            }
        } else if (!securityQuestionAPI.value?.question.isNullOrEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)

            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                val images = listOf(
                    R.drawable.step_06_security_questions_1,
                    R.drawable.step_06_security_questions_2,
                    R.drawable.step_06_security_questions_3
                )
                ImagesBox(images = images, modifier = Modifier.fillMaxHeight(0.2f))
                Spacer(modifier = Modifier.fillMaxHeight(0.07f))

                Text(
                    text = stringResource(id = R.string.youMustAnswerSecurityQuestion),
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                    fontSize = 12.sp,
                    color = MaterialTheme.appColors.textColor,
                    textAlign = TextAlign.Center

                )
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                securityQuestionAPI.value?.question?.let {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(R.drawable.info_icon),
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary),
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = it,
                                fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                                fontSize = 12.sp,
                                color = MaterialTheme.appColors.textColor
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.2.dp,
                            color = MaterialTheme.appColors.primary
                        )
                    }
                }


                Spacer(modifier = Modifier.height(20.dp))
                AnswerTextField(answer, securityQuestionAuthVM, answerError)
                Spacer(modifier = Modifier.fillMaxHeight(0.4f))

                ButtonView(
                    onClick = {


                        answerValidate.value = true
                        securityQuestionAuthVM.onChangeValue(securityQuestionAuthVM.answer.value)
                        if (securityQuestionAuthVM.answer.value.text.trim().isNotEmpty()) {
                            securityQuestionAuthVM.validateSecurityQuestionsCall()
                        }


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
    securityQuestionAuthVM: SecurityQuestionAuthViewModel,
    answerError: State<String?>
) {
    val maxChar = 150

    Column {
        TextField(
            value = answer.value,
            onValueChange = {
                if (it.text.length <= maxChar)
                    securityQuestionAuthVM.onChangeValue(it)
            },
            supportingText = {
                Text(
                    text = "${answer.value.text.length} / $maxChar",
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(stringResource(id = R.string.answer), fontSize = 12.sp,fontFamily = MaterialTheme.typography.labelLarge.fontFamily,) },
            colors = textFieldColors(),
            leadingIcon = {
                Image(
                    painterResource(R.drawable.answer_icon),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary),
                    contentDescription = "",
                )
            },
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 12.sp,
                color = Color.Black
            )
        )
        if (answerError.value != null) {
            Text(
                answerError.value!!,
                fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                color = MaterialTheme.appColors.errorColor,
                style = MaterialTheme.typography.labelSmall
            )
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



