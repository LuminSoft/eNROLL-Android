package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.ui.components.MultiplePhoneNumbersScreenContent
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.ui.components.PhoneNumbersOnBoardingScreenContent
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.ui.components.ValidateOtpPhoneNumberScreenContent
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val phoneNumbersOnBoardingScreenContent = "phoneNumbersOnBoardingScreenContent"
const val multiplePhoneNumbersScreenContent = "multiplePhoneNumbersScreenContent"
const val validateOtpPhoneNumberScreenContent = "validateOtpPhoneNumberScreenContent"


fun NavGraphBuilder.phoneNumberRouter(
    navController: NavController, onBoardingViewModel: OnBoardingViewModel
) {
    composable(route = phoneNumbersOnBoardingScreenContent) {
        PhoneNumbersOnBoardingScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel
        )
    }
    composable(route = multiplePhoneNumbersScreenContent) {
        MultiplePhoneNumbersScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel
        )
    }
    composable(route = validateOtpPhoneNumberScreenContent) {
        ValidateOtpPhoneNumberScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel,
        )
    }
}