package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.ui.components


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.PhoneInfoUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.PhoneSendOtpUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_navigation.validateOtpPhoneNumberScreenContent
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.view_model.PhoneNumbersOnBoardingViewModel
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import com.togitech.ccp.component.TogiCountryCodePicker
import org.koin.compose.koinInject


@Composable
fun PhoneNumbersOnBoardingScreenContent(
    onBoardingViewModel: OnBoardingViewModel,
    navController: NavController,
) {

    val phoneInfoUseCase =
        PhoneInfoUseCase(koinInject())

    val phoneSendOtpUseCase =
        PhoneSendOtpUseCase(koinInject())

    val phoneNumbersOnBoardingViewModel =
        remember {
            PhoneNumbersOnBoardingViewModel(
                phoneInfoUseCase = phoneInfoUseCase,
                phoneSendOtpUseCase = phoneSendOtpUseCase
            )
        }
    val phoneNumbersOnBoardingVM = remember { phoneNumbersOnBoardingViewModel }

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = phoneNumbersOnBoardingViewModel.loading.collectAsState()
    val phoneNumberSentSuccessfully =
        phoneNumbersOnBoardingViewModel.phoneNumberSentSuccessfully.collectAsState()
    val failure = phoneNumbersOnBoardingViewModel.failure.collectAsState()

    var phoneNumber: String by rememberSaveable { mutableStateOf("") }
    var phoneCode: String by rememberSaveable { mutableStateOf("") }
    var fullPhoneNumber: String by rememberSaveable { mutableStateOf("") }
    var isNumberValid: Boolean by rememberSaveable { mutableStateOf(false) }
    var isClicked by mutableStateOf(false)



    BackGroundView(navController = navController, showAppBar = true) {
        if (isClicked) {
            DialogView(
                bottomSheetStatus = BottomSheetStatus.WARNING,
                text = stringResource(id = R.string.phoneOtpContentConfirmationMessage) + fullPhoneNumber,
                buttonText = stringResource(id = R.string.continue_to_next),
                secondButtonText = stringResource(id = R.string.cancel),
                onPressedButton = {
                    phoneNumbersOnBoardingViewModel.loading.value = true
                    onBoardingViewModel.currentPhoneNumber.value = phoneNumber
                    isClicked = false
                    phoneNumbersOnBoardingVM.callPhoneInfo(phoneCode.replace("+", ""), phoneNumber)

                },
                onPressedSecondButton = {
                    isClicked = false
                }
            )
        }

        if (phoneNumberSentSuccessfully.value) {
            navController.navigate(validateOtpPhoneNumberScreenContent)
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
//                            phoneNumbersOnBoardingVM.callGetCountries()
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
                    painterResource(R.drawable.step_03_phone),
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight(0.3f)
                )

                Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    if (onBoardingViewModel.currentPhoneNumber.value != null) {
                        phoneCode = onBoardingViewModel.currentPhoneNumberCode.value!!
                        phoneNumber = onBoardingViewModel.currentPhoneNumber.value!!
                        fullPhoneNumber = phoneCode + phoneNumber
                    }
                    TogiCountryCodePicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        initialCountryPhoneCode = onBoardingViewModel.currentPhoneNumberCode.value,
                        initialPhoneNumber = onBoardingViewModel.currentPhoneNumber.value,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            disabledBorderColor = MaterialTheme.colorScheme.primary,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            errorLabelColor = MaterialTheme.colorScheme.error,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            disabledLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                        ),
                        shape = RoundedCornerShape(8.dp),
                        onValueChange = { (code, phone), isValid ->
                            Log.d("CCP", "onValueChange: $code $phone -> $isValid")
                            phoneNumber = phone
                            phoneCode = code
                            onBoardingViewModel.currentPhoneNumberCode.value = code
                            fullPhoneNumber = code + phone
                            isNumberValid = isValid
                            if (!isValid)
                                onBoardingViewModel.currentPhoneNumber.value = null
                        },
                        label = {
                            Text(
                                ResourceProvider.instance.getStringResource(R.string.phoneNumber),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                    )
                }


                Spacer(modifier = Modifier.fillMaxHeight(0.05f))

                Text(
                    text = stringResource(id = R.string.sendPhoneOtpContent),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.35f))

                ButtonView(
                    isEnabled = onBoardingViewModel.currentPhoneNumber.value != null || isNumberValid,
                    onClick = {
                        isClicked = true
                    }, title = stringResource(id = R.string.confirmAndContinue)
                )
                Spacer(modifier = Modifier.height(20.dp))

            }
        }
    }

}
