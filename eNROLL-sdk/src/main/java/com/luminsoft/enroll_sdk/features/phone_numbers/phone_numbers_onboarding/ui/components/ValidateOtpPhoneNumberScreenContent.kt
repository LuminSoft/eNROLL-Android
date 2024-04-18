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
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingBackConfirmationScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.userNameValue
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.ValidateOtpPhoneUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.view_model.ValidateOtpPhoneNumbersViewModel
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import com.togitech.ccp.component.TogiCountryCodePicker
import org.koin.compose.koinInject


@Composable
fun ValidateOtpPhoneNumberScreenContent(
    onBoardingViewModel: OnBoardingViewModel,
    navController: NavController,
) {

    val validateOtpPhoneUseCase =
        ValidateOtpPhoneUseCase(koinInject())

    val validateOtpPhoneNumbersViewModel =
        remember {
            ValidateOtpPhoneNumbersViewModel(
                validateOtpPhoneUseCase = validateOtpPhoneUseCase
            )
        }
    val phoneNumbersOnBoardingVM = remember { validateOtpPhoneNumbersViewModel }

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = validateOtpPhoneNumbersViewModel.loading.collectAsState()
    val otpApproved =
        validateOtpPhoneNumbersViewModel.otpApproved.collectAsState()
    val failure = validateOtpPhoneNumbersViewModel.failure.collectAsState()

    var phoneNumber: String by rememberSaveable { mutableStateOf("") }
    var phoneCode: String by rememberSaveable { mutableStateOf("") }
    var fullPhoneNumber: String by rememberSaveable { mutableStateOf("") }
    var isNumberValid: Boolean by rememberSaveable { mutableStateOf(false) }



    BackGroundView(navController = navController, showAppBar = true) {
        if (otpApproved.value) {
            navController.navigate(nationalIdOnBoardingBackConfirmationScreen)
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
                    modifier = Modifier.fillMaxHeight(0.2f)
                )
                /*
                                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(75.dp)
                                    ) {
                                        DropdownList(
                                            itemList = countries.value!!,
                                            selectedIndex = selectedIndex,
                                            modifier = Modifier.fillMaxWidth(0.3f),
                                            validateOtpPhoneNumbersViewModel
                                        )

                                        Spacer(modifier = Modifier.width(20.dp))
                                        NormalTextField(
                                            label = ResourceProvider.instance.getStringResource(R.string.type_your_phoneNumber),
                                            value = userNameValue.value,
                                            width = 1f,
                                            height = 60.0,
                                            icon = {
                                                Image(
                                                    painterResource(R.drawable.user_icon),
                                                    contentDescription = "",
                                                    modifier = Modifier
                                                        .height(50.dp)
                                                )
                                            },
                                            onValueChange = {
                                                userNameValue.value = it
                //                            userHasModifiedText.value = true
                                            },
                                            keyboardOptions = KeyboardOptions(
                                                imeAction = ImeAction.Done,
                                            ),
                //                        error = phoneNumberValidation(),
                                        )


                                    }
                                }*/
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    TogiCountryCodePicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        initialCountryPhoneCode = "+20",
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
                            fullPhoneNumber = code + phone
                            isNumberValid = isValid
                        },
                        label = {
                            Text(
                                ResourceProvider.instance.getStringResource(R.string.phoneNumber),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                    )
                }
                /*   Box() {
                       if (showDropdown.value) {
                           Popup(
                               alignment = Alignment.TopCenter,
                               properties = PopupProperties(
                                   excludeFromSystemGesture = true,
                               ),
                               // to dismiss on click outside
                               onDismissRequest = {
                                   validateOtpPhoneNumbersViewModel.showDropdown.value = false
                               }
                           ) {

                               Column(
                                   modifier = Modifier
                                       .fillMaxHeight(0.5f)
                                       .fillMaxWidth(0.4f)
   //                            .heightIn(max = 90.dp)
                                       .verticalScroll(state = scrollState),
   //                            .border(width = 1.dp, color = Color.Gray),
                                   horizontalAlignment = Alignment.CenterHorizontally,
                               ) {

                                   countries.value!!.onEachIndexed { index, item ->
                                       if (index != 0) {
                                           Divider(thickness = 1.dp, color = Color.Black)
                                       }
                                       Box(
                                           modifier = Modifier
                                               .background(Color.White)
                                               .fillMaxWidth()
                                               .padding(bottom = 7.dp, top = 7.dp)
                                               .clickable {
                                                   selectedIndex = index
                                                   validateOtpPhoneNumbersViewModel.showDropdown.value =
                                                       !validateOtpPhoneNumbersViewModel.showDropdown.value
                                               },
                                           contentAlignment = Alignment.Center
                                       ) {
                                           Text(text = item.name + " +" + item.code, fontSize = 10.sp)
                                       }
                                   }

                               }
                           }
                       }
                   }*/

                Spacer(modifier = Modifier.fillMaxHeight(0.35f))

                ButtonView(
                    isEnabled = isNumberValid,
                    onClick = {
                    }, title = stringResource(id = R.string.continue_to_next)
                )
                Spacer(modifier = Modifier.height(20.dp))

            }
        }
    }

}

private fun phoneNumberValidation() = when {

    userNameValue.value.text.isEmpty() -> {
        ResourceProvider.instance.getStringResource(R.string.required_english_name)
    }

    userNameValue.value.text.length < 2 -> {
        ResourceProvider.instance.getStringResource(R.string.invalid_english_name_min)
    }

    userNameValue.value.text.length > 150 -> {
        ResourceProvider.instance.getStringResource(R.string.invalid_english_name_max)
    }

    !Regex("^[A-Za-z-. ]+\$").matches(
        userNameValue.value.text
    ) -> {
        ResourceProvider.instance.getStringResource(R.string.invalid_english_name)

    }

    else -> null
}