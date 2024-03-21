package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.ui.components.PhoneNumbersOnBoardingScreenContent

const val phoneNumbersOnBoardingScreenContent = "phoneNumbersOnBoardingScreenContent"

fun NavController.navigateToPhoneNumbers(navOptions: NavOptions? = null) {
    this.navigate(phoneNumbersOnBoardingScreenContent, navOptions)
}

fun NavGraphBuilder.phoneNumberRouter(navController: NavController) {
    composable(route = phoneNumbersOnBoardingScreenContent) {
        PhoneNumbersOnBoardingScreenContent(navController=navController)
    }
}