package com.luminsoft.enroll_sdk.features.setting_password.password_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features.setting_password.password_onboarding.ui.components.SettingPasswordOnBoardingScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val settingPasswordOnBoardingScreenContent = "settingPasswordOnBoardingScreenContent"


fun NavGraphBuilder.settingPasswordRouter(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {
    composable(route = settingPasswordOnBoardingScreenContent) {
        SettingPasswordOnBoardingScreenContent(onBoardingViewModel, navController = navController)
    }
}