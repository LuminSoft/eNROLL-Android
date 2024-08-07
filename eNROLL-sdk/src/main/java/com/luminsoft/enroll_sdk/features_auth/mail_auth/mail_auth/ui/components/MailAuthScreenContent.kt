package com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import appColors
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.models.EnrollSuccessModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.features.email.email_onboarding.ui.components.OtpInputField
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth.view_model.MailAuthViewModel
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_domain.usecases.MailAuthSendOTPUseCase
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_domain.usecases.ValidateOtpMailAuthUseCase
import com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_models.get_auth_configurations.EkycStepAuthType
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import kotlin.time.Duration.Companion.seconds


@Composable
fun MailAuthScreenContent(
    authViewModel: AuthViewModel,
    navController: NavController,
) {

    val validateOtpMailAuthUseCase =
        ValidateOtpMailAuthUseCase(koinInject())
    val mailAuthSendOTPUseCase =
        MailAuthSendOTPUseCase(koinInject())

    val mailViewModel =
        remember {
            MailAuthViewModel(
                mailAuthSendOTPUseCase = mailAuthSendOTPUseCase,
                validateOtpMailAuthUseCase = validateOtpMailAuthUseCase
            )
        }

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = mailViewModel.loading.collectAsState()
    val otpApproved =
        mailViewModel.otpApproved.collectAsState()
    val failure = mailViewModel.failure.collectAsState()

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
            val isEmpty = authViewModel.removeCurrentStep(EkycStepAuthType.Email.getStepId())
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
        } else if (loading.value) LoadingView()
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
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 2.dp)

            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                Image(
                    painterResource(R.drawable.validate_mail_otp),
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight(0.25f)
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.07f))
                Text(
                    text = stringResource(id = R.string.emailOtpGuide),
                    color = MaterialTheme.appColors.primary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(30.dp))

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    OtpInputField(
                        otp = otpValue,
                        count = 6,
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.timerOtpMessage),
                        color = MaterialTheme.appColors.primary,
                        fontSize = 10.sp
                    )
                    Timer(ticksF, ticks)
                    Text(
                        text = stringResource(id = R.string.second),
                        color = MaterialTheme.appColors.primary,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Spacer(modifier = Modifier.fillMaxHeight(0.35f))
                ButtonView(
                    isEnabled = otpValue.value.length == 6,
                    onClick = {
                        mailViewModel.callValidateOtp(otpValue.value)
                    },
                    title = stringResource(id = R.string.confirmAndContinue)
                )
                Spacer(modifier = Modifier.height(20.dp))
                ButtonView(
                    isEnabled = ticks == 0,
                    onClick = {
                        mailViewModel.callSendOtp()
                        ticks = 60
                        ticksF = 1.0f
                        counter++
                    },
                    title = stringResource(id = R.string.resend),
                    textColor = MaterialTheme.appColors.primary,
                    color = MaterialTheme.appColors.onPrimary,
                    borderColor = MaterialTheme.appColors.primary,
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
            progress = 1f,
            modifier = Modifier.size(30.dp),
            color = MaterialTheme.appColors.onSecondary.copy(alpha = 0.5f),
            strokeWidth = 3.dp
        )
        CircularProgressIndicator(
            progress = ticksF,
            modifier = Modifier.size(30.dp),
            strokeWidth = 3.dp,
            color = MaterialTheme.appColors.onSecondary
        )
        Text(
            text = ticks.toString(),
            color = MaterialTheme.appColors.onSecondary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

