package com.luminsoft.enroll_sdk.features.check_cso.check_cso_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_onboarding.ui.components.CheckCsoOnBoardingScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val checkCsoOnBoardingScreenContent = "checkCsoOnBoardingScreenContent"

fun NavGraphBuilder.checkCsoRouter(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {
    composable(route = checkCsoOnBoardingScreenContent) {
        CheckCsoOnBoardingScreenContent(
            onBoardingViewModel = onBoardingViewModel,
            navController = navController
        )
    }
}
