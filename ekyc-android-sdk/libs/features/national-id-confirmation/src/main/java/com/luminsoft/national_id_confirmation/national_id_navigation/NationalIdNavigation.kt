package com.luminsoft.national_id_confirmation.national_id_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luminsoft.national_id_confirmation.national_id_confirmation_presentation.national_id_onboarding.ui.components.NationalIdOnBoardingPrescanScreen

const val nationalIdOnBoardingPrescanScreen = "nationalIdOnBoardingPrescanScreen"

fun NavController.navigateToNationalIdConfirmation(navOptions: NavOptions? = null) {
    this.navigate(nationalIdOnBoardingPrescanScreen, navOptions)
}

fun NavGraphBuilder.nationalIdRouter(navController: NavController) {

    composable(route = nationalIdOnBoardingPrescanScreen) {
        NationalIdOnBoardingPrescanScreen(navController=navController)
    }
}