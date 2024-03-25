package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.ui.components.PhoneNumbersOnBoardingScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val phoneNumbersOnBoardingScreenContent = "phoneNumbersOnBoardingScreenContent"


fun NavGraphBuilder.phoneNumberRouter(
    navController: NavController, onBoardingViewModel: OnBoardingViewModel
) {
    composable(route = phoneNumbersOnBoardingScreenContent) {
        PhoneNumbersOnBoardingScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel
        )
    }
}