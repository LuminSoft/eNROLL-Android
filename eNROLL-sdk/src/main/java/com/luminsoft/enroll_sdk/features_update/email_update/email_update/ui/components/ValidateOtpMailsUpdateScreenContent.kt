package com.luminsoft.enroll_sdk.features_update.email_update.email_update.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import appColors
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.core.widgets.ImagesBox
import com.luminsoft.enroll_sdk.features.email.email_onboarding.ui.components.OtpInputField
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases.SendOtpUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases.ValidateOtpMailUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.email_update.email_navigation_update.mailsUpdateScreenContent
import com.luminsoft.enroll_sdk.features_update.email_update.email_update.view_model.ValidateOtpMailsUpdateViewModel
import com.luminsoft.enroll_sdk.main_update.main_update_navigation.updateListScreenContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import kotlin.time.Duration.Companion.seconds


@Composable
fun ValidateOtpMailsUpdateScreenContent(
    updateViewModel: UpdateViewModel,
    navController: NavController,
) {

    val validateOtpMailUseCase =
        ValidateOtpMailUpdateUseCase(koinInject())
    val mailSendOtpUseCase =
        SendOtpUpdateUseCase(koinInject())

    val validateOtpMailsViewModel =
        remember {
            ValidateOtpMailsUpdateViewModel(
                validateOtpMailUseCase = validateOtpMailUseCase,
                mailSendOtpUseCase = mailSendOtpUseCase
            )
        }
    val mailsUpdateVM = remember { validateOtpMailsViewModel }

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = validateOtpMailsViewModel.loading.collectAsState()
    val otpApproved =
        validateOtpMailsViewModel.otpApproved.collectAsState()
    val failure = validateOtpMailsViewModel.failure.collectAsState()
    val wrongOtpTimes = validateOtpMailsViewModel.wrongOtpTimes.collectAsState()
    val mailValue = updateViewModel.mailValue.collectAsState()
    val isNotFirstMail = updateViewModel.isNotFirstMail.collectAsState()

    val otpValue = remember { mutableStateOf("") }

    var counter by remember { mutableIntStateOf(0) }

    var ticks by remember { mutableIntStateOf(60) }
    var ticksF by remember { mutableFloatStateOf(1.0f) }
    LaunchedEffect(counter) {
        while (ticks > 0) {
            delay(1.seconds)
            ticks--
            ticksF -= 0.016666668f
        }
    }
    BackGroundView(navController = navController, showAppBar = true) {
        if (otpApproved.value) {
            DialogView(
                bottomSheetStatus = BottomSheetStatus.SUCCESS,
                text = stringResource(id = R.string.successfulUpdate),
                buttonText = stringResource(id = R.string.exit),
                onPressedButton = {
                    navController.navigate(updateListScreenContent)
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
            } else if (wrongOtpTimes.value == 5) {
                failure.value?.let {
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = stringResource(id = R.string.wrongOtpFiveTimes),
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            navController.navigate(updateListScreenContent)
                        },
                    )
                }
            } else {
                failure.value?.let {
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.cancel),
                        onPressedButton = {
                            validateOtpMailsViewModel.failure.value = null
                            otpValue.value = ""
                        },
                    )
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)

            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                val images = listOf(
                    R.drawable.validate_mail_otp_1,
                    R.drawable.validate_mail_otp_2,
                    R.drawable.validate_mail_otp_3
                )
                ImagesBox(images = images, modifier = Modifier.fillMaxHeight(0.25f))
                Spacer(modifier = Modifier.fillMaxHeight(0.07f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        ResourceProvider.instance.getStringResource(R.string.emailOtpSendTo),
                        fontSize = 8.sp,
                        color = MaterialTheme.appColors.textColor
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        mailValue.value!!.text,
                        fontSize = 10.sp,
                        color = MaterialTheme.appColors.secondary
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Box {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    MaterialTheme.appColors.secondary,
                                    shape = RoundedCornerShape(0.dp)
                                ),

                            )
                        Text(
                            ResourceProvider.instance.getStringResource(R.string.edit),
                            fontSize = 8.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .clickable(enabled = true) {
                                    navController.navigate(mailsUpdateScreenContent)
                                }
                        )
                    }

                }
                Spacer(modifier = Modifier.height(30.dp))

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    OtpInputField(
                        otp = otpValue,                        textColor =           MaterialTheme.appColors.textColor,

                        count = 6,
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.timerOtpMessage),
                        color = MaterialTheme.appColors.textColor,
                        fontSize = 10.sp
                    )
                    Timer(ticksF, ticks)
                    Text(
                        text = stringResource(id = R.string.second),
                        color = MaterialTheme.appColors.textColor,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                if (isNotFirstMail.value && ticks == 0)
                    Text(
                        text = stringResource(id = R.string.resend),
                        color = MaterialTheme.appColors.primary,
                        fontSize = 14.sp,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clickable(enabled = true) {
                                updateViewModel.mailId.value?.let { mailsUpdateVM.callSendOtp(it) }
                                ticks = 60
                                ticksF = 1.0f
                                counter++
                            }

                    )
                Spacer(modifier = Modifier.fillMaxHeight(0.35f))
                ButtonView(
                    onClick = {
                        updateViewModel.userMail.value = updateViewModel.mailValue.value?.text
//                        updateViewModel.mailValue.value = TextFieldValue()
                        updateViewModel.mailId.value?.let {
                            mailsUpdateVM.callValidateOtp(
                                otpValue.value,
                                it
                            )
                        }
                    },
                    title = stringResource(id = R.string.confirmAndContinue),
                    isEnabled = otpValue.value.length == 6
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (!isNotFirstMail.value)
                    ButtonView(
                        onClick = {
                            updateViewModel.mailId.value?.let { mailsUpdateVM.callSendOtp(it) }
                            ticks = 60
                            ticksF = 1.0f
                            counter++
                        },
                        title = stringResource(id = R.string.resend),
                        color = MaterialTheme.appColors.backGround,
                        borderColor = MaterialTheme.appColors.primary,
                        isEnabled = ticks == 0,
                        textColor = MaterialTheme.appColors.primary,
                    ) else
                    ButtonView(
                        onClick = {
                            updateViewModel.mailValue.value = TextFieldValue()
                            navController.navigate(updateListScreenContent)
                        },
                        title = stringResource(id = R.string.skip),
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
private fun Timer(ticksF: Float, ticks: Int) {
    Box(Modifier.size(50.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(30.dp),
            color = MaterialTheme.appColors.secondary.copy(alpha = 0.5f),
            strokeWidth = 3.dp,
        )
        CircularProgressIndicator(
            progress = { ticksF },
            modifier = Modifier.size(30.dp),
            color = MaterialTheme.appColors.secondary,
            strokeWidth = 3.dp,
        )
        Text(
            text = ticks.toString(),
            color = MaterialTheme.appColors.textColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

