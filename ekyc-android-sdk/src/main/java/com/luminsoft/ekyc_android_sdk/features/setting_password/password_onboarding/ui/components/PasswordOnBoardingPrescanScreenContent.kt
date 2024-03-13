package com.luminsoft.ekyc_android_sdk.features.setting_password.password_onboarding.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.core.failures.AuthFailure
import com.luminsoft.ekyc_android_sdk.core.models.EKYCFailedModel
import com.luminsoft.ekyc_android_sdk.core.sdk.EkycSdk
import com.luminsoft.ekyc_android_sdk.core.utils.ResourceProvider
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_onboarding.view_model.PasswordOnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.ui_components.components.BackGroundView
import com.luminsoft.ekyc_android_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.ekyc_android_sdk.ui_components.components.ButtonView
import com.luminsoft.ekyc_android_sdk.ui_components.components.DialogView
import com.luminsoft.ekyc_android_sdk.ui_components.components.NormalTextField
import org.koin.androidx.compose.koinViewModel

var password = mutableStateOf(TextFieldValue())
var confirmPassword = mutableStateOf(TextFieldValue())
var validate = mutableStateOf(false)

@Composable
fun SettingPasswordOnBoardingScreenContent(
    passwordOnBoardingViewModel: PasswordOnBoardingViewModel = koinViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val activity = context.findActivity()

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var rePasswordVisible by rememberSaveable { mutableStateOf(false) }
    val passwordApproved = passwordOnBoardingViewModel.passwordApproved.collectAsState()
    val failure = passwordOnBoardingViewModel.failure.collectAsState()

    BackGroundView(navController = navController, showAppBar = true) {
        if (passwordApproved.value) {
            DialogView(
                bottomSheetStatus = BottomSheetStatus.SUCCESS,
                text = stringResource(id = R.string.successfulRegistration),
                buttonText = stringResource(id = R.string.continue_to_next),
                onPressedButton = {
                    activity.finish()
                    EkycSdk.ekycCallback?.error(
                        EKYCFailedModel(
                            activity.getString(R.string.successfulRegistration),
                            activity.getString(R.string.successfulRegistration)
                        )
                    )

                },
            )
        } else if (!failure.value?.message.isNullOrEmpty()) {
            if (failure.value is AuthFailure) {
                failure.value?.let {
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            activity.finish()
                            EkycSdk.ekycCallback?.error(EKYCFailedModel(it.message, it))

                        },
                    )
                    {
                        activity.finish()
                        EkycSdk.ekycCallback?.error(EKYCFailedModel(it.message, it))

                    }
                }
            } else {
                failure.value?.let {
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.retry),
                        onPressedButton = {
                            passwordOnBoardingViewModel.callSetPassword(password.value.text)
                        },
                        secondButtonText = stringResource(id = R.string.exit),
                        onPressedSecondButton = {
                            activity.finish()
                            EkycSdk.ekycCallback?.error(EKYCFailedModel(it.message, it))

                        }
                    )
                    {
                        activity.finish()
                        EkycSdk.ekycCallback?.error(EKYCFailedModel(it.message, it))
                    }
                }
            }
        } else
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(25.dp))

                Image(
                    painterResource(R.drawable.step_07_password),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxHeight(0.3f)
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                NormalTextField(
                    label = ResourceProvider.instance.getStringResource(R.string.password),
                    value = password.value,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    onValueChange = { password.value = it },
                    height = 60.0,
                    trailingIcon = {
                        val imageResource = if (passwordVisible)
                            R.drawable.visibility_icon
                        else R.drawable.visibility_off_icon
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        Image(
                            painterResource(imageResource),
                            contentDescription = description,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .clickable {
                                    passwordVisible = !passwordVisible
                                }
                                .size(20.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                    ),
                    error = passwordValidation(),

                    )
                Spacer(modifier = Modifier.height(20.dp))

                NormalTextField(
                    label = ResourceProvider.instance.getStringResource(R.string.confirmPassword),
                    value = confirmPassword.value,
                    onValueChange = { confirmPassword.value = it },
                    visualTransformation = if (rePasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    height = 60.0,
                    trailingIcon = {
                        val imageResource = if (rePasswordVisible)
                            R.drawable.visibility_icon
                        else R.drawable.visibility_off_icon
                        val description =
                            if (rePasswordVisible) "Hide password" else "Show password"

                        Image(
                            painterResource(imageResource),
                            contentDescription = description,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .clickable {
                                    rePasswordVisible = !rePasswordVisible
                                }
                                .size(20.dp)
                        )
                    },
                    error = confirmPasswordValidation(),
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.3f))
                ButtonView(
                    onClick = {
                        validate.value = true
                        if (passwordValidation() == null && confirmPasswordValidation() == null) {
                            passwordOnBoardingViewModel.callSetPassword(password.value.text)
                        }
                    },
                    title = ResourceProvider.instance.getStringResource(R.string.send)
                )
            }

    }

}


private fun passwordValidation() = when {
    !validate.value -> {
        null
    }

    password.value.text.isEmpty() -> {
        ResourceProvider.instance.getStringResource(R.string.errorEmptyPassword)
    }

    password.value.text.length < 6 -> {
        ResourceProvider.instance.getStringResource(R.string.errorLengthPassword)
    }

    password.value.text.length > 128 -> {
        ResourceProvider.instance.getStringResource(R.string.errorMaxLengthPassword)
    }

    !Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\u0021-\\u002F \\u003A-\\u003F\\u0040\\u005B-\\u005F\\u0060\\u007B-\\u007E])[A-Za-z\\d\\u0021-\\u002F\\u003A-\\u003F\\u0040\\u005B-\\u005F\\u0060\\u007B-\\u007E]{6,128}\$").matches(
        password.value.text
    ) -> {
        ResourceProvider.instance.getStringResource(R.string.errorFormatPassword)

    }

    else -> null
}


private fun confirmPasswordValidation() = when {
    !validate.value -> {
        null
    }

    confirmPassword.value.text.isEmpty() -> {
        ResourceProvider.instance.getStringResource(R.string.required_confirm_password)
    }

    passwordValidation() != null -> {
        ResourceProvider.instance.getStringResource(R.string.enterValidPasswordFirst)
    }

    password.value != confirmPassword.value -> {
        ResourceProvider.instance.getStringResource(R.string.confirmPasswordError)
    }


    else -> null
}

