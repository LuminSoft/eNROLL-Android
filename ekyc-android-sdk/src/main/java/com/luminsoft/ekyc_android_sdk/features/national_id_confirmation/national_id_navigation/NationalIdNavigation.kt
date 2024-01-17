package com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.NationalIdOnBoardingBackConfirmationScreen
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.NationalIdOnBoardingFrontConfirmationScreen
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.NationalIdOnBoardingPrescanScreen
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val nationalIdOnBoardingPrescanScreen = "nationalIdOnBoardingPrescanScreen"
const val nationalIdOnBoardingFrontConfirmationScreen =
    "nationalIdOnBoardingFrontConfirmationScreen"
const val nationalIdOnBoardingBackConfirmationScreen =
    "nationalIdOnBoardingBackConfirmationScreen"

/*
fun NavController.navigateToNationalIdConfirmation(navOptions: NavOptions? = null) {
    this.navigate(nationalIdOnBoardingPrescanScreen, navOptions)
}

fun NavController.nationalIdOnBoardingFrontConfirmationScreen(navOptions: NavOptions? = null) {
    this.navigate(nationalIdOnBoardingFrontConfirmationScreen, navOptions)
}

fun NavController.nationalIdOnBoardingBackConfirmationScreen(navOptions: NavOptions? = null) {
    this.navigate(nationalIdOnBoardingBackConfirmationScreen, navOptions)
}
*/

fun NavGraphBuilder.nationalIdRouter(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {

    composable(route = nationalIdOnBoardingPrescanScreen) {
        NationalIdOnBoardingPrescanScreen(
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
    composable(route = nationalIdOnBoardingBackConfirmationScreen) {
        NationalIdOnBoardingBackConfirmationScreen(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel
        )
    }
}