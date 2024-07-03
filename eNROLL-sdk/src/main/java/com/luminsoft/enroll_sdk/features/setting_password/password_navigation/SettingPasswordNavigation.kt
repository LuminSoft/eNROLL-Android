package com.luminsoft.enroll_sdk.features.setting_password.password_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features.setting_password.password_onboarding.ui.components.SettingPasswordOnBoardingScreenContent

const val settingPasswordOnBoardingScreenContent = "settingPasswordOnBoardingScreenContent"


fun NavGraphBuilder.settingPasswordRouter(navController: NavController) {
    composable(route = settingPasswordOnBoardingScreenContent) {
        SettingPasswordOnBoardingScreenContent(navController = navController)
    }
}