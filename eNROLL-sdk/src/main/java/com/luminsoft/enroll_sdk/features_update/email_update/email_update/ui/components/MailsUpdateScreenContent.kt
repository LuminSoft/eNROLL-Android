package com.luminsoft.enroll_sdk.features_update.email_update.email_update.ui.components

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases.SendOtpUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases.UpdateMailAddUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.email_update.email_navigation_update.validateOtpMailsUpdateScreenContent
import com.luminsoft.enroll_sdk.features_update.email_update.email_update.view_model.AddMailUpdateViewModel
import com.luminsoft.enroll_sdk.main_update.main_update_navigation.updateListScreenContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import com.luminsoft.enroll_sdk.ui_components.components.NormalTextField
import org.koin.compose.koinInject


@Composable
fun MailsUpdateScreenContent(
    updateViewModel: UpdateViewModel,
    navController: NavController,
) {

    val updateMailAddUseCase =
        UpdateMailAddUpdateUseCase(koinInject())

    val sendOtpUpdateUseCase =
        SendOtpUpdateUseCase(koinInject())

    val mailsUpdateViewModel =
        remember {
            AddMailUpdateViewModel(
                updateMailAddUseCase = updateMailAddUseCase,
                sendOtpUpdateUseCase = sendOtpUpdateUseCase
            )
        }
    val mailsUpdateVM = remember { mailsUpdateViewModel }
    val mailValue = updateViewModel.mailValue.collectAsState()

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = mailsUpdateViewModel.loading.collectAsState()
    val mailSentSuccessfully =
        mailsUpdateViewModel.mailSentSuccessfully.collectAsState()
    val failure = mailsUpdateViewModel.failure.collectAsState()
    val isClicked = mailsUpdateViewModel.isClicked.collectAsState()
    val mailId = mailsUpdateViewModel.mailId.collectAsState()

    val userHasModifiedText = remember { mutableStateOf(false) }

    BackGroundView(navController = navController, showAppBar = true) {
        if (isClicked.value) {
            DialogView(
                bottomSheetStatus = BottomSheetStatus.WARNING,
                text = stringResource(id = R.string.emailOtpContentConfirmationMessage) + mailValue.value?.text,
                buttonText = stringResource(id = R.string.continue_to_next),
                secondButtonText = stringResource(id = R.string.cancel),
                onPressedButton = {
                    mailsUpdateViewModel.loading.value = true
//                    updateViewModel.currentMail.value = mailValue.value.text
                    mailsUpdateViewModel.isClicked.value = false
                    mailsUpdateVM.addMailCallApi(mailValue.value!!.text)
                },
                onPressedSecondButton = {
                    mailsUpdateViewModel.isClicked.value = false
                }
            )
        }

        if (mailSentSuccessfully.value) {
            mailId.value?.let { updateViewModel.updateMailId(it) }
            navController.navigate(validateOtpMailsUpdateScreenContent)
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
                            mailsUpdateViewModel.failure.value = null
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
                        updateViewModel.mailValue.value = it
                        userHasModifiedText.value = true
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Email
                    ),
                    error = englishNameValidation(
                        updateViewModel
                    ),
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
                        if (englishNameValidation(
                                updateViewModel
                            ) == null
                        )
                            mailsUpdateViewModel.isClicked.value = true
                    }, title = stringResource(id = R.string.confirmAndContinue)
                )
                Spacer(modifier = Modifier.height(20.dp))
                ButtonView(
                    onClick = {
                        navController.navigate(updateListScreenContent)
                    },
                    title = stringResource(id = R.string.exit),
                    color = MaterialTheme.appColors.backGround,
                    borderColor = MaterialTheme.appColors.primary,
                    textColor = MaterialTheme.appColors.primary,
                )

            }
        }
    }

}

private fun englishNameValidation(updateViewModel: UpdateViewModel) = when {

    updateViewModel.mailValue.value!!.text.isEmpty() -> {
        ResourceProvider.instance.getStringResource(R.string.required_email)
    }

    !Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}").matches(
        updateViewModel.mailValue.value!!.text
    ) -> {
        ResourceProvider.instance.getStringResource(R.string.invalid_email)
    }

    else -> null
}