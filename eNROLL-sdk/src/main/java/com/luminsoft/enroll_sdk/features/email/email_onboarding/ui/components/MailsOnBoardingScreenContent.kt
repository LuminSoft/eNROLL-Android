package com.luminsoft.enroll_sdk.features.email.email_onboarding.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.luminsoft.enroll_sdk.features.email.email_domain.usecases.MailInfoUseCase
import com.luminsoft.enroll_sdk.features.email.email_domain.usecases.MailSendOtpUseCase
import com.luminsoft.enroll_sdk.features.email.email_navigation.validateOtpMailsScreenContent
import com.luminsoft.enroll_sdk.features.email.email_onboarding.view_model.MailsOnBoardingViewModel
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import com.luminsoft.enroll_sdk.ui_components.components.NormalTextField
import org.koin.compose.koinInject


@Composable
fun MailsOnBoardingScreenContent(
    onBoardingViewModel: OnBoardingViewModel,
    navController: NavController,
) {

    val mailInfoUseCase =
        MailInfoUseCase(koinInject())

    val mailSendOtpUseCase =
        MailSendOtpUseCase(koinInject())

    val mailsOnBoardingViewModel =
        remember {
            MailsOnBoardingViewModel(
                mailInfoUseCase = mailInfoUseCase,
                mailSendOtpUseCase = mailSendOtpUseCase
            )
        }
    val mailsOnBoardingVM = remember { mailsOnBoardingViewModel }
    val mailValue = onBoardingViewModel.mailValue.collectAsState()

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = mailsOnBoardingViewModel.loading.collectAsState()
    val mailSentSuccessfully =
        mailsOnBoardingViewModel.mailSentSuccessfully.collectAsState()
    val failure = mailsOnBoardingViewModel.failure.collectAsState()

//    var mail: String by rememberSaveable { mutableStateOf(TextFieldValue()) }
//    var isMailValid: Boolean by rememberSaveable { mutableStateOf(false) }
    var isClicked by mutableStateOf(false)

    val userHasModifiedText = remember { mutableStateOf(false) }


    BackGroundView(navController = navController, showAppBar = true) {
        if (isClicked) {
            DialogView(
                bottomSheetStatus = BottomSheetStatus.WARNING,
                text = stringResource(id = R.string.emailOtpContentConfirmationMessage) + mailValue.value?.text,
                buttonText = stringResource(id = R.string.continue_to_next),
                secondButtonText = stringResource(id = R.string.cancel),
                onPressedButton = {
                    mailsOnBoardingViewModel.loading.value = true
//                    onBoardingViewModel.currentMail.value = mailValue.value.text
                    isClicked = false
                    mailsOnBoardingVM.callMailInfo(mailValue.value!!.text)
                },
                onPressedSecondButton = {
                    isClicked = false
                }
            )
        }

        if (mailSentSuccessfully.value) {
            navController.navigate(validateOtpMailsScreenContent)
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
                        buttonText = stringResource(id = R.string.cancel),
                        onPressedButton = {
                            mailsOnBoardingViewModel.failure.value = null
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
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)

            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                Image(
                    painterResource(R.drawable.step_04_email),
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight(0.3f)
                )

                Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                NormalTextField(
                    label = ResourceProvider.instance.getStringResource(R.string.mailFormatError),
                    value = mailValue.value!!,
                    height = 60.0,
                    icon = {
                        Image(
                            painterResource(R.drawable.mail_icon),
                            contentDescription = "",
                            modifier = Modifier
                                .height(50.dp)
                        )
                    },
                    onValueChange = {
                        onBoardingViewModel.mailValue.value = it
                        userHasModifiedText.value = true
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Email
                    ),
                    error = englishNameValidation(onBoardingViewModel),
                )

                Spacer(modifier = Modifier.fillMaxHeight(0.05f))

                Text(
                    text = stringResource(id = R.string.sendEmailOtpContent),
                    color = MaterialTheme.appColors.primary,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.35f))

                ButtonView(
                    onClick = {
                        if (englishNameValidation(onBoardingViewModel) == null)
                            isClicked = true
                    }, title = stringResource(id = R.string.confirmAndContinue)
                )
                Spacer(modifier = Modifier.height(20.dp))

            }
        }
    }

}

private fun englishNameValidation(onBoardingViewModel: OnBoardingViewModel) = when {

    onBoardingViewModel.mailValue.value!!.text.isEmpty() -> {
        ResourceProvider.instance.getStringResource(R.string.required_email)
    }

    !Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}").matches(
        onBoardingViewModel.mailValue.value!!.text
    ) -> {
        ResourceProvider.instance.getStringResource(R.string.invalid_email)
    }

    else -> null
}