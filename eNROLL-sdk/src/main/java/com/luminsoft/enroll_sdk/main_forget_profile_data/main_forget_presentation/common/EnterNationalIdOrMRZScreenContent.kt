package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.common

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import appColors
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.main_forget.view_model.ForgetViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.NormalTextField
import com.luminsoft.enroll_sdk.ui_components.components.SpinKitLoadingIndicator
import com.luminsoft.enroll_sdk.ui_components.theme.appColors


@SuppressLint("StateFlowValueCalledInComposition", "SuspiciousIndentation")
@Composable
fun EnterNationalIdOrMRZScreenContent(
    forgetViewModel: ForgetViewModel,
    navController: NavController,
) {


    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = forgetViewModel.loading.collectAsState()
    val failure = forgetViewModel.failure.collectAsState()


    BackGroundView(navController = navController, showAppBar = true) {

        if (loading.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { SpinKitLoadingIndicator() }
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
                    ) {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                    }
                }
            }
        } else {


            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.5f))

                NationalIdTextField(forgetViewModel)

                Spacer(modifier = Modifier.weight(1f))
                ButtonView(
                    onClick = {

                        var isValid = true

                        if (forgetViewModel.nationalIdValue.value!!.text.isEmpty()) {
                            forgetViewModel.nationalIdError.value =
                                ResourceProvider.instance.getStringResource(R.string.emptyError)
                            isValid = false
                        }

                        if (isValid) {
                            forgetViewModel.generateForgetTokenForStep()
                        }
                    },
                    stringResource(id = R.string.confirmAndContinue),
                )
                Spacer(modifier = Modifier.weight(1f))

            }
        }
    }
}


@Composable
fun NationalIdTextField(
    forgetViewModel: ForgetViewModel
) {
    val nationalIdValue = forgetViewModel.nationalIdValue.collectAsState()

    NormalTextField(
        label = ResourceProvider.instance.getStringResource(R.string.enterNationalIDorMRZ),
        value = nationalIdValue.value!!,
        height = 60.0,
        icon = {
            Image(
                painter = painterResource(R.drawable.id_card_icon),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary),
                modifier = Modifier.height(50.dp)
            )
        },
        onValueChange = { newValue ->
            forgetViewModel.nationalIdValue.value = newValue

            // Check if the field is empty
            forgetViewModel.nationalIdError.value =
                if (newValue.text.isEmpty()) {
                    ResourceProvider.instance.getStringResource(R.string.emptyError)
                } else {
                    null
                }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        error = forgetViewModel.nationalIdError.value
    )
}



