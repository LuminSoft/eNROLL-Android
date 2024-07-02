package com.luminsoft.enroll_sdk.main.main_navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features.setting_password.password_onboarding.ui.components.SettingPasswordOnBoardingScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.common.SplashScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.ui.components.OnboardingScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val splashScreenOnBoardingContent = "splashScreenOnBoardingContent"
const val splashScreenAuthContent = "splashScreenAuthContent"
const val onBoardingScreenContent = "onBoardingScreenContent"
const val passwordScreenContent = "passwordScreenContent"


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.mainRouter(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {

    composable(route = splashScreenOnBoardingContent) {
        SplashScreenContent(onBoardingViewModel, navController = navController)
    }
    composable(route = onBoardingScreenContent) {
        OnboardingScreenContent(onBoardingViewModel, navController = navController)
    }
    composable(route = passwordScreenContent) {
        SettingPasswordOnBoardingScreenContent(navController = navController)
    }
}