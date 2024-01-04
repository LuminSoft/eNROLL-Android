package com.luminsoft.ekyc_android_sdk.features.setting_password.password_onboarding.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_onboarding.view_model.PasswordOnBoardingViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun SettingPasswordOnBoardingScreenContent(
    passwordOnBoardingViewModel: PasswordOnBoardingViewModel = koinViewModel<PasswordOnBoardingViewModel>(),
    navController: NavController,
    isSavedCards: Boolean = true
) {

}
