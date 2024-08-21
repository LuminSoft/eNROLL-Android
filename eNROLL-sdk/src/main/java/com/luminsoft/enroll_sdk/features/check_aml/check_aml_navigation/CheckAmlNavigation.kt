package com.luminsoft.enroll_sdk.features.check_aml.check_aml_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features.check_aml.check_aml_onboarding.ui.components.CheckAmlOnBoardingScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val checkAmlOnBoardingScreenContent =
    "com.luminsoft.enroll_sdk.features.check_aml.check_aml_navigation.checkAmlOnBoardingScreenContent"

fun NavGraphBuilder.checkAmlRouter(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel

) {
    composable(route = checkAmlOnBoardingScreenContent) {
        CheckAmlOnBoardingScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel

        )
    }
}