package com.luminsoft.enroll_sdk.main_auth.main_auth_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features.setting_password.password_onboarding.ui.components.SettingPasswordOnBoardingScreenContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.common.SplashScreenAuthContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel

const val splashScreenAuthContent = "splashScreenAuthContent"
const val splashScreenAuthContent2 = "splashScreenAuthContent"
const val onBoardingScreenContent2 = "onBoardingScreenContent"
const val passwordScreenContent2 = "passwordScreenContent"
const val settingPasswordOnBoardingScreenContent = "settingPasswordOnBoardingScreenContent"


fun NavGraphBuilder.mainAuthRouter(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    composable(route = splashScreenAuthContent) {
        SplashScreenAuthContent(authViewModel, navController = navController)
    }
    composable(route = com.luminsoft.enroll_sdk.features.setting_password.password_navigation.settingPasswordOnBoardingScreenContent) {
        SettingPasswordOnBoardingScreenContent(navController = navController)
    }
//    composable(route = onBoardingScreenContent) {
//        OnboardingScreenContent(authViewModel, navController = navController)
//    }
//    composable(route = passwordScreenContent) {
//        SettingPasswordOnBoardingScreenContent(navController = navController)
//    }
}