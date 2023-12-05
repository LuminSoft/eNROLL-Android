package com.luminsoft.ekyc_android_sdk.features.email.email_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luminsoft.ekyc_android_sdk.features.email.email_onboarding.ui.components.EmailOnBoardingPrescanScreenContent

const val emailOnBoardingPrescanScreenContent = "emailOnBoardingPrescanScreenContent"

fun NavController.navigateToEmail(navOptions: NavOptions? = null) {
    this.navigate(emailOnBoardingPrescanScreenContent, navOptions)
}

fun NavGraphBuilder.emailRouter(navController: NavController) {
    composable(route = emailOnBoardingPrescanScreenContent) {
        EmailOnBoardingPrescanScreenContent(navController=navController)
    }
}