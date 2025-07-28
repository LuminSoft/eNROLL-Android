package com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract.ui.components

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.models.EnrollSuccessModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.core.widgets.ImagesBox
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.ui.components.OtpInputField
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract.view_model.SignContractOTPViewModel
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_domain.usecases.SignContractSendOTPUseCase
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_domain.usecases.ValidateOtpSignContractUseCase
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_presentation.main_sign_contract.view_model.SignContractViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import com.luminsoft.enroll_sdk.ui_components.theme.appColors
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import kotlin.time.Duration.Companion.seconds


@Composable
fun SignContractScreenContent(
    signContractViewModel: SignContractViewModel,
    navController: NavController,
) {

    val validateOtpSignContractUseCase =
        ValidateOtpSignContractUseCase(koinInject())
    val signContractSendOTPUseCase =
        SignContractSendOTPUseCase(koinInject())

    val signContractOTPViewModel =
        remember {
            SignContractOTPViewModel(
                signContractSendOTPUseCase = signContractSendOTPUseCase,
                validateOtpSignContractUseCase = validateOtpSignContractUseCase
            )
        }

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = signContractOTPViewModel.loading.collectAsState()
    val otpApproved =
        signContractOTPViewModel.otpApproved.collectAsState()
    val failure = signContractOTPViewModel.failure.collectAsState()
    val phoneNumber = signContractOTPViewModel.phoneNumber.collectAsState()

    val otpValue = remember { mutableStateOf("") }

    var counter by remember { mutableIntStateOf(0) }

    var ticks by remember { mutableIntStateOf(60) }
    var ticksF by remember { mutableFloatStateOf(1.0f) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(counter) {
        while (ticks > 0) {
            delay(1.seconds)
            ticks--
            ticksF -= 0.016666668f
        }
    }
    BackGroundView(navController = navController, showAppBar = true) {

        if (showConfirmationDialog) {
            DialogView(
                bottomSheetStatus = BottomSheetStatus.WARNING,
                text = stringResource(id = R.string.exitConfirmation),
                buttonText = stringResource(id = R.string.exit),
                onPressedButton = {

                    showConfirmationDialog = false

                    activity.finish()
                    EnrollSDK.enrollCallback?.error(
                        EnrollFailedModel(
                            "Press Exit Button",
                            Throwable()
                        )
                    )
                },
                secondButtonText = stringResource(id = R.string.cancel),
                onPressedSecondButton = {
                    showConfirmationDialog = false
                }
            )
        }

        if (otpApproved.value) {

            DialogView(
                bottomSheetStatus = BottomSheetStatus.SUCCESS,
                text = stringResource(id = R.string.contractSignedSuccessfully),
                buttonText = stringResource(id = R.string.continue_to_next),
                onPressedButton = {
                    activity.finish()
                    EnrollSDK.enrollCallback?.success(
                        EnrollSuccessModel(
                            activity.getString(R.string.contractSignedSuccessfully)
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
                        secondButtonText = stringResource(id = R.string.cancel),
                        buttonText = stringResource(id = R.string.exit),
                        onPressedSecondButton = {
                            signContractOTPViewModel.resetFailure()
                        },
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
                    .padding(horizontal = 24.dp)

            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))

                val images = listOf(
                    R.drawable.validate_sms_otp_1,
                    R.drawable.validate_sms_otp_2,
                    R.drawable.validate_sms_otp_3,
                )
                ImagesBox(images = images, modifier = Modifier.fillMaxHeight(0.25f))
                Spacer(modifier = Modifier.fillMaxHeight(0.07f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        ResourceProvider.instance.getStringResource(R.string.otpSendTo),
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                        fontSize = 10.sp,
                        color = MaterialTheme.appColors.textColor
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        phoneNumber.value!!,
                        fontSize = 12.sp,
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                        color = MaterialTheme.appColors.secondary
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))


                Spacer(modifier = Modifier.height(30.dp))

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    OtpInputField(
                        otp = otpValue, textColor = MaterialTheme.appColors.textColor,

                        count = 6,
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.timerOtpMessage),
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                        color = MaterialTheme.appColors.textColor,
                        fontSize = 10.sp
                    )
                    Timer(ticksF, ticks)
                    Text(
                        text = stringResource(id = R.string.second),
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                        color = MaterialTheme.appColors.textColor,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                if (ticks == 0)
                    Text(
                        text = stringResource(id = R.string.resend),
                        color = MaterialTheme.appColors.primary,
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                        fontSize = 14.sp,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clickable(enabled = true) {
                                signContractOTPViewModel.callSendOtp()
                                ticks = 60
                                ticksF = 1.0f
                                counter++
                            }

                    )
                Spacer(modifier = Modifier.fillMaxHeight(0.35f))
                ButtonView(
                    onClick = {
                        signContractOTPViewModel.callValidateOtp(otpValue.value)
                    },
                    title = stringResource(id = R.string.confirmAndContinue),
                    isEnabled = otpValue.value.length == 6
                )
                Spacer(modifier = Modifier.height(8.dp))
                ButtonView(
                    onClick = { showConfirmationDialog = true },
                    title = stringResource(id = R.string.exit),
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
            fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
            color = MaterialTheme.appColors.textColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

