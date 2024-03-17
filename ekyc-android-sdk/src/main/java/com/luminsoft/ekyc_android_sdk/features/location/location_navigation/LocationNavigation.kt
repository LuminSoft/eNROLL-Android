package com.luminsoft.ekyc_android_sdk.features.location.location_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.ekyc_android_sdk.features.location.location_onboarding.ui.components.LocationOnBoardingScreenContent
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val locationOnBoardingScreenContent = "locationOnBoardingScreenContent"


fun NavGraphBuilder.locationRouter(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel

) {
    composable(route = locationOnBoardingScreenContent) {
        LocationOnBoardingScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel

        )
    }
}