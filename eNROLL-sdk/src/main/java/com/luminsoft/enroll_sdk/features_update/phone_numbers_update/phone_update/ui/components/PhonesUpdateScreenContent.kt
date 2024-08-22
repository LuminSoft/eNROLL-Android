package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_update.ui.components

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
import androidx.compose.ui.text.style.TextAlign
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
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases.SendOtpUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases.UpdatePhoneAddUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_navigation_update.validateOtpPhonesUpdateScreenContent
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_update.view_model.AddPhoneUpdateViewModel
import com.luminsoft.enroll_sdk.main_update.main_update_navigation.updateListScreenContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import com.togitech.ccp.component.TogiCountryCodePicker
import org.koin.compose.koinInject


@Composable
fun PhonesUpdateScreenContent(
    updateViewModel: UpdateViewModel,
    navController: NavController,
) {

    val updatePhoneAddUseCase =
        UpdatePhoneAddUpdateUseCase(koinInject())

    val sendOtpUpdateUseCase =
        SendOtpUpdateUseCase(koinInject())

    val phonesUpdateViewModel =
        remember {
            AddPhoneUpdateViewModel(
                updatePhoneAddUseCase = updatePhoneAddUseCase,
                sendOtpUpdateUseCase = sendOtpUpdateUseCase
            )
        }
    val phonesUpdateVM = remember { phonesUpdateViewModel }

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = phonesUpdateViewModel.loading.collectAsState()
    val phoneSentSuccessfully =
        phonesUpdateViewModel.phoneSentSuccessfully.collectAsState()
    val failure = phonesUpdateViewModel.failure.collectAsState()
    val isClicked = phonesUpdateViewModel.isClicked.collectAsState()
    val phoneId = phonesUpdateViewModel.phoneId.collectAsState()
    var phoneNumber: String by rememberSaveable { mutableStateOf("") }
    var phoneCode: String by rememberSaveable { mutableStateOf("") }
    var fullPhoneNumber: String by rememberSaveable { mutableStateOf("") }
    var isNumberValid: Boolean by rememberSaveable { mutableStateOf(false) }

    BackGroundView(navController = navController, showAppBar = true) {
        if (isClicked.value) {
            DialogView(
                bottomSheetStatus = BottomSheetStatus.WARNING,
                text = stringResource(id = R.string.phoneOtpContentConfirmationMessage) + fullPhoneNumber,
                buttonText = stringResource(id = R.string.continue_to_next),
                secondButtonText = stringResource(id = R.string.cancel),
                onPressedButton = {
                    phonesUpdateViewModel.loading.value = true
//                    phonesUpdateViewModel.currentPhoneNumber.value = phoneNumber
                    phonesUpdateViewModel.isClicked.value = false
                    phonesUpdateVM.addPhoneCallApi(phoneCode.replace("+", ""), phoneNumber)
                },
                onPressedSecondButton = {
                    phonesUpdateViewModel.isClicked.value = false
                }
            )
        }

        if (phoneSentSuccessfully.value) {
            phoneId.value?.let { updateViewModel.updatePhoneId(it) }
            navController.navigate(validateOtpPhonesUpdateScreenContent)
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
                            phonesUpdateViewModel.failure.value = null
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
                    if (updateViewModel.currentPhoneNumber.value != null) {
                        phoneCode = updateViewModel.currentPhoneNumberCode.value!!
                        phoneNumber = updateViewModel.currentPhoneNumber.value!!
                        fullPhoneNumber = phoneCode + phoneNumber
                    }
                    TogiCountryCodePicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        initialCountryPhoneCode = updateViewModel.currentPhoneNumberCode.value,
                        initialPhoneNumber = updateViewModel.currentPhoneNumber.value,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.appColors.primary,
                            disabledBorderColor = MaterialTheme.appColors.primary,
                            errorBorderColor = MaterialTheme.appColors.errorColor,
                            unfocusedBorderColor = MaterialTheme.appColors.primary,
                            errorLabelColor = MaterialTheme.appColors.errorColor,
                            focusedLabelColor = MaterialTheme.appColors.primary,
                            disabledLabelColor = MaterialTheme.appColors.primary,
                            unfocusedLabelColor = MaterialTheme.appColors.primary,
                        ),
                        shape = RoundedCornerShape(8.dp),
                        onValueChange = { (code, phone), isValid ->
                            Log.d("CCP", "onValueChange: $code $phone -> $isValid")
                            phoneNumber = phone
                            phoneCode = code
                            updateViewModel.currentPhoneNumberCode.value = code
                            fullPhoneNumber = code + phone
                            isNumberValid = isValid
                            if (!isValid)
                                updateViewModel.currentPhoneNumber.value = null
                        },
                        label = {
                            Text(
                                ResourceProvider.instance.getStringResource(R.string.phoneNumber),
                                fontSize = 14.sp,
                                color = MaterialTheme.appColors.primary
                            )
                        },
                    )
                }


                Spacer(modifier = Modifier.fillMaxHeight(0.05f))

                Text(
                    text = stringResource(id = R.string.sendPhoneOtpContent),
                    color = MaterialTheme.appColors.primary,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.35f))

                ButtonView(
                    onClick = {
                        phonesUpdateViewModel.isClicked.value = true
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

private fun phoneNumberValidation(updateViewModel: UpdateViewModel) = when {

    updateViewModel.phoneValue.value!!.text.isEmpty() -> {
        ResourceProvider.instance.getStringResource(R.string.required_phone_number)
    }

    else -> null
}
