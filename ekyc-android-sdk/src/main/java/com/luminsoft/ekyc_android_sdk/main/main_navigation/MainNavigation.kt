package com.luminsoft.ekyc_android_sdk.main.main_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luminsoft.ekyc_android_sdk.main.main_onboarding.ui.components.SplashScreenContent
import com.luminsoft.ekyc_android_sdk.main.main_onboarding.view_model.OnBoardingViewModel
import org.koin.androidx.compose.koinViewModel

const val splashScreenOnBoardingContent = "splashScreenOnBoardingContent"
const val splashScreenAuthContent = "splashScreenAuthContent"

fun NavController.navigateToMain(navOptions: NavOptions? = null) {
    this.navigate(splashScreenOnBoardingContent, navOptions)
}

fun NavGraphBuilder.mainRouter(navController: NavController) {
    composable(route = splashScreenOnBoardingContent) {
        SplashScreenContent(navController=navController)
    }
}