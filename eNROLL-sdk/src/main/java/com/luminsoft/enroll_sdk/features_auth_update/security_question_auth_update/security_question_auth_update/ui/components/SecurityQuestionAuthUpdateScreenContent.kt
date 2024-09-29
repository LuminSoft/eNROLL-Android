import android.annotation.SuppressLint
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
import androidx.compose.runtime.LaunchedEffect
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
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.widgets.ImagesBox
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features_auth.security_question_auth.security_question_auth.ui.components.answerValidate
import com.luminsoft.enroll_sdk.features_auth_update.security_question_auth_update.security_question_auth_update_domain.usecases.GetSecurityQuestionAuthUpdateUseCase
import com.luminsoft.enroll_sdk.features_auth_update.security_question_auth_update.security_question_auth_update_domain.usecases.ValidateSecurityQuestionAuthUpdateUseCase
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import org.koin.compose.koinInject


var isAnswerValidate = mutableStateOf(false)

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SecurityQuestionAuthUpdateScreenContent(
    navController: NavController,
    updateViewModel: UpdateViewModel,
) {

    val getSecurityQuestionAuthUpdateUseCase =
        GetSecurityQuestionAuthUpdateUseCase(koinInject())

    val validateSecurityQuestionUseCase =
        ValidateSecurityQuestionAuthUpdateUseCase(koinInject())

    val securityQuestionViewModel =
        remember {
            SecurityQuestionAuthUpdateViewModel(
                getSecurityQuestionAuthUseCase = getSecurityQuestionAuthUpdateUseCase,
                validateSecurityQuestionUseCase = validateSecurityQuestionUseCase,
            )
        }
    securityQuestionViewModel.setStepUpdateId(updateViewModel.updateStepId.value!!)

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = securityQuestionViewModel.loading.collectAsState()
    val securityQuestionApproved =
        securityQuestionViewModel.securityQuestionApproved.collectAsState()
    val failure = securityQuestionViewModel.failure.collectAsState()
    val answer = securityQuestionViewModel.answer.collectAsState()
    val securityQuestionAPI = securityQuestionViewModel.securityQuestion.collectAsState()
    val answerError = securityQuestionViewModel.answerError.collectAsState()

    LaunchedEffect(securityQuestionApproved.value) {
        if (securityQuestionApproved.value) {
            updateViewModel.navigateToUpdateAfterAuthStep()
        }
    }
    BackGroundView(navController = navController, showAppBar = true) {

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
                    .padding(horizontal = 24.dp)

            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                val images= listOf(R.drawable.step_06_security_questions_1,R.drawable.step_06_security_questions_2,R.drawable.step_06_security_questions_3)
                ImagesBox(images = images,modifier = Modifier.fillMaxHeight(0.2f))

                Spacer(modifier = Modifier.fillMaxHeight(0.07f))

                Text(
                    text = stringResource(id = R.string.youMustAnswerSecurityQuestion),
                    fontSize = 12.sp,
                    color = Color.Black,
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
                                contentScale = ContentScale.FillBounds,
                                colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = it,
                                fontSize = 12.sp,
                                color = Color.Black
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
                AnswerTextFieldWidget(answer, securityQuestionViewModel, answerError)
                Spacer(modifier = Modifier.fillMaxHeight(0.4f))

                ButtonView(
                    onClick = {
                        answerValidate.value = true
                        securityQuestionViewModel.onChangeValue(securityQuestionViewModel.answer.value)
                        if (securityQuestionViewModel.answer.value.text.trim().isNotEmpty()) {
                            securityQuestionViewModel.validateSecurityQuestionsCall()
                        }
                    },
                    title = stringResource(id = R.string.confirmAndContinue)
                )

                Spacer(modifier = Modifier.height(8.dp))

                ButtonView(
                    onClick = {
                        navController.popBackStack()
                    },
                    stringResource(id = R.string.cancel),
                    color = MaterialTheme.appColors.backGround,
                    borderColor = MaterialTheme.appColors.primary,
                    textColor = MaterialTheme.appColors.primary,
                )


            }
        }
    }
}

@Composable
private fun AnswerTextFieldWidget(
    answer: State<TextFieldValue>,
    securityQuestionAuthVM: SecurityQuestionAuthUpdateViewModel,
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
            println(" second answerError.value ${answerError.value}")
            Text(
                answerError.value!!,
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



