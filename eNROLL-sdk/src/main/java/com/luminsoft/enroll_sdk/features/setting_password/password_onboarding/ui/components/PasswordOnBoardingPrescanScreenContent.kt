package com.luminsoft.enroll_sdk.features.setting_password.password_onboarding.ui.components


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
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import appColors
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features.setting_password.password_domain.usecases.OnboardingSettingPasswordUseCase
import com.luminsoft.enroll_sdk.features.setting_password.password_onboarding.view_model.PasswordOnBoardingViewModel
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.EkycStepType
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.NormalTextField
import org.koin.compose.koinInject

@Composable
fun SettingPasswordOnBoardingScreenContent(
    onBoardingViewModel: OnBoardingViewModel,
    navController: NavController
) {

    val setPasswordUseCase =
        OnboardingSettingPasswordUseCase(koinInject())
    val passwordOnBoardingViewModel =
        remember {
            PasswordOnBoardingViewModel(
                setPasswordUseCase = setPasswordUseCase
            )
        }

    val context = LocalContext.current
    val activity = context.findActivity()

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var rePasswordVisible by rememberSaveable { mutableStateOf(false) }
    val passwordApproved = passwordOnBoardingViewModel.passwordApproved.collectAsState()
    val failure = passwordOnBoardingViewModel.failure.collectAsState()
    val password = passwordOnBoardingViewModel.password.collectAsState()
    val confirmPassword = passwordOnBoardingViewModel.confirmPassword.collectAsState()

    BackGroundView(navController = navController, showAppBar = true) {
        if (passwordApproved.value) {
            val isEmpty =
                onBoardingViewModel.removeCurrentStep(EkycStepType.SettingPassword.getStepId())
            if (isEmpty)
                DialogView(
                    bottomSheetStatus = BottomSheetStatus.SUCCESS,
                    text = stringResource(id = R.string.successfulRegistration),
                    buttonText = stringResource(id = R.string.continue_to_next),
                    onPressedButton = {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(
                            EnrollFailedModel(
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
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                        },
                    )
                    {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

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
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                        }
                    )
                    {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
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
                    colorFilter =   ColorFilter.tint(MaterialTheme.appColors.primary),

                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxHeight(0.3f)
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                NormalTextField(
                    label = ResourceProvider.instance.getStringResource(R.string.password),
                    value = password.value,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    onValueChange = { passwordOnBoardingViewModel.password.value = it },
                    height = 60.0,
                    trailingIcon = {
                        val imageResource = if (passwordVisible)
                            R.drawable.visibility_icon
                        else R.drawable.visibility_off_icon
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        Image(
                            painterResource(imageResource),
                            contentDescription = description,
                            colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary),
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
                    error = passwordOnBoardingViewModel.passwordValidation(),

                    )
                Spacer(modifier = Modifier.height(20.dp))

                NormalTextField(
                    label = ResourceProvider.instance.getStringResource(R.string.confirmPassword),
                    value = confirmPassword.value,
                    onValueChange = { passwordOnBoardingViewModel.confirmPassword.value = it },
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
                            colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary),
                            modifier = Modifier
                                .clickable {
                                    rePasswordVisible = !rePasswordVisible
                                }
                                .size(20.dp)
                        )
                    },
                    error = passwordOnBoardingViewModel.confirmPasswordValidation(),
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.3f))
                ButtonView(
                    onClick = {
                        passwordOnBoardingViewModel.validate.value = true
                        if (passwordOnBoardingViewModel.passwordValidation() == null && passwordOnBoardingViewModel.confirmPasswordValidation() == null) {
                            passwordOnBoardingViewModel.callSetPassword(password.value.text)
                        }
                    },
                    title = ResourceProvider.instance.getStringResource(R.string.send)
                )
            }

    }

}



