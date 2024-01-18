package com.luminsoft.ekyc_android_sdk.features.setting_password.password_onboarding.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.core.utils.ResourceProvider
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_onboarding.view_model.PasswordOnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.ui_components.components.BackGroundView
import com.luminsoft.ekyc_android_sdk.ui_components.components.ButtonView
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

    BackGroundView(navController = navController, showAppBar = true) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
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
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = { password.value = it },
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
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
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

    !Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{6,128}\$").matches(
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

