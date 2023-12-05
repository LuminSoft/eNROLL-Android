package com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_onboarding.ui.components.SecurityQuestionsOnBoardingScreenContent

const val securityQuestionsOnBoardingScreenContent = "securityQuestionsOnBoardingScreenContent"

fun NavController.navigateToSecurityQuestions(navOptions: NavOptions? = null) {
    this.navigate(securityQuestionsOnBoardingScreenContent, navOptions)
}

fun NavGraphBuilder.securityQuestionsRouter(navController: NavController) {
    composable(route = securityQuestionsOnBoardingScreenContent) {
        SecurityQuestionsOnBoardingScreenContent(navController=navController)
    }
}