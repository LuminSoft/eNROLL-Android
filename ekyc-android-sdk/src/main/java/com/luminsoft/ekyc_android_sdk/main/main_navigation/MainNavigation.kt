package com.luminsoft.ekyc_android_sdk.main.main_navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luminsoft.ekyc_android_sdk.main.main_presentation.common.SplashScreenContent
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_auth.view_model.AuthViewModel
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.ui.components.OnboardingScreenContent
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel

const val splashScreenOnBoardingContent = "splashScreenOnBoardingContent"
const val splashScreenAuthContent = "splashScreenAuthContent"
const val onBoardingScreenContent = "onBoardingScreenContent"

fun NavController.navigateToMain(navOptions: NavOptions? = null) {
    this.navigate(splashScreenOnBoardingContent, navOptions)
}

fun NavGraphBuilder.mainRouter(navController: NavController) {
    composable(route = splashScreenOnBoardingContent) {
        SplashScreenContent(koinViewModel<OnBoardingViewModel>(),navController=navController)
    }
    composable(route = splashScreenAuthContent) {
        SplashScreenContent(koinViewModel<AuthViewModel>(),navController=navController)
    }
    composable(route = onBoardingScreenContent) {
        OnboardingScreenContent(navController=navController)
    }
}