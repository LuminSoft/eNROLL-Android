package com.luminsoft.ekyc_android_sdk.features.location.location_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luminsoft.ekyc_android_sdk.features.location.location_onboarding.ui.components.LocationOnBoardingScreenContent
import com.luminsoft.ekyc_android_sdk.features.location.location_onboarding.view_model.LocationOnBoardingViewModel

const val locationOnBoardingScreenContent = "locationOnBoardingScreenContent"

fun NavController.navigateToLocation(navOptions: NavOptions? = null) {
    this.navigate(locationOnBoardingScreenContent, navOptions)
}

fun NavGraphBuilder.locationRouter(
    navController: NavController,
    locationOnBoardingViewModel: LocationOnBoardingViewModel
) {
    composable(route = locationOnBoardingScreenContent) {
        LocationOnBoardingScreenContent(
            navController = navController,
            locationOnBoardingViewModel = locationOnBoardingViewModel
        )
    }
}