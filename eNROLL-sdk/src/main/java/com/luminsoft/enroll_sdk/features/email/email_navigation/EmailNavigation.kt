package com.luminsoft.enroll_sdk.features.email.email_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features.email.email_onboarding.ui.components.MailsOnBoardingScreenContent
import com.luminsoft.enroll_sdk.features.email.email_onboarding.ui.components.MultipleMailsScreenContent
import com.luminsoft.enroll_sdk.features.email.email_onboarding.ui.components.ValidateOtpMailsScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val mailsOnBoardingScreenContent = "mailsOnBoardingScreenContent"
const val multipleMailsScreenContent = "multipleMailsScreenContent"
const val validateOtpMailsScreenContent = "validateOtpMailsScreenContent"


fun NavGraphBuilder.emailRouter(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {
    composable(route = mailsOnBoardingScreenContent) {
        MailsOnBoardingScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel
        )
    }
    composable(route = multipleMailsScreenContent) {
        MultipleMailsScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel
        )
    }
    composable(route = validateOtpMailsScreenContent) {
        ValidateOtpMailsScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel,
        )
    }
}