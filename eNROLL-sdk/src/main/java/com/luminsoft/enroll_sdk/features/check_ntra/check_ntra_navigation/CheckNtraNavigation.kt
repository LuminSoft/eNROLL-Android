package com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_onboarding.ui.components.CheckNtraOnBoardingScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val checkNtraOnBoardingScreenContent =
    "com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_navigation.checkNtraOnBoardingScreenContent"

fun NavGraphBuilder.checkNtraRouter(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel

) {
    composable(route = checkNtraOnBoardingScreenContent) {
        CheckNtraOnBoardingScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel

        )
    }
}
