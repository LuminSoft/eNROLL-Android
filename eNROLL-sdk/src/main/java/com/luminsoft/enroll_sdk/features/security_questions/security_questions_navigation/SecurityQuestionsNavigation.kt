package com.luminsoft.enroll_sdk.features.security_questions.security_questions_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_onboarding.ui.components.SecurityQuestionsOnBoardingScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val securityQuestionsOnBoardingScreenContent = "securityQuestionsOnBoardingScreenContent"


fun NavGraphBuilder.securityQuestionsRouter(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {
    composable(route = securityQuestionsOnBoardingScreenContent) {
        SecurityQuestionsOnBoardingScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel
        )
    }
}