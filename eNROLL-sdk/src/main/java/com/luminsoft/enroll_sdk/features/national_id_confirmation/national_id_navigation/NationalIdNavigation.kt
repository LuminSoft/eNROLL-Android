package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.NationalIdOnBoardingBackConfirmationScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.NationalIdOnBoardingErrorScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.NationalIdOnBoardingFrontConfirmationScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.NationalIdOnBoardingPreScanScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.PassportOnBoardingConfirmationScreen
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val nationalIdOnBoardingPreScanScreen = "nationalIdOnBoardingPreScanScreen"
const val nationalIdOnBoardingFrontConfirmationScreen =
    "nationalIdOnBoardingFrontConfirmationScreen"
const val passportOnBoardingConfirmationScreen =
    "passportOnBoardingConfirmationScreen"
const val nationalIdOnBoardingBackConfirmationScreen =
    "nationalIdOnBoardingBackConfirmationScreen"
const val nationalIdOnBoardingErrorScreen =
    "nationalIdOnBoardingErrorScreen"

fun NavGraphBuilder.nationalIdRouter(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {

    composable(route = nationalIdOnBoardingPreScanScreen) {
        NationalIdOnBoardingPreScanScreen(
            navController = navController,
            onBoardingViewModel = remember { onBoardingViewModel }
        )
    }
    composable(route = nationalIdOnBoardingFrontConfirmationScreen) {
        NationalIdOnBoardingFrontConfirmationScreen(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel
        )
    }
    composable(route = passportOnBoardingConfirmationScreen) {
        PassportOnBoardingConfirmationScreen(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel
        )
    }
    composable(route = nationalIdOnBoardingBackConfirmationScreen) {
        NationalIdOnBoardingBackConfirmationScreen(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel
        )
    }
    composable(route = nationalIdOnBoardingErrorScreen) {
        NationalIdOnBoardingErrorScreen(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel
        )
    }
}