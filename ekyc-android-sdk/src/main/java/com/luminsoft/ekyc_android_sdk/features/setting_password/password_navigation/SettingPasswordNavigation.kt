package com.luminsoft.ekyc_android_sdk.features.setting_password.password_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_onboarding.ui.components.SettingPasswordOnBoardingScreenContent

const val settingPasswordOnBoardingScreenContent = "settingPasswordOnBoardingScreenContent"

fun NavController.navigateToSettingPassword(navOptions: NavOptions? = null) {
    this.navigate(settingPasswordOnBoardingScreenContent, navOptions)
}

fun NavGraphBuilder.settingPasswordRouter(navController: NavController) {
    composable(route = settingPasswordOnBoardingScreenContent) {
        SettingPasswordOnBoardingScreenContent(navController=navController)
    }
}