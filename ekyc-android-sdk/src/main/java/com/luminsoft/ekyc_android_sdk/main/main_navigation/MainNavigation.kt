package com.luminsoft.ekyc_android_sdk.main.main_navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luminsoft.ekyc_android_sdk.main.main_presentation.common.SplashScreenContent
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.ui.components.OnboardingScreenContent
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.TutorialViewModel

const val splashScreenOnBoardingContent = "splashScreenOnBoardingContent"
const val splashScreenAuthContent = "splashScreenAuthContent"
const val onBoardingScreenContent = "onBoardingScreenContent"

fun NavController.navigateToMain(navOptions: NavOptions? = null) {
    this.navigate(splashScreenOnBoardingContent, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.mainRouter(navController: NavController, onBoardingViewModel:OnBoardingViewModel) {

    composable(route = splashScreenOnBoardingContent) {
        SplashScreenContent(onBoardingViewModel,navController=navController)
    }
//    composable(route = splashScreenAuthContent) {
//        SplashScreenContent(koinViewModel<AuthViewModel>(),navController=navController)
//    }
    composable(route = onBoardingScreenContent) {
        OnboardingScreenContent(onBoardingViewModel,navController=navController)
    }
}