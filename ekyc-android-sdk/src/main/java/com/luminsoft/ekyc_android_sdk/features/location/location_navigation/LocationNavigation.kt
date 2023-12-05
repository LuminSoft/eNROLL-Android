package com.luminsoft.ekyc_android_sdk.features.location.location_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luminsoft.ekyc_android_sdk.features.location.national_id_onboarding.ui.components.LocationOnBoardingScreenContent

const val locationOnBoardingScreenContent = "locationOnBoardingScreenContent"

fun NavController.navigateToLocation(navOptions: NavOptions? = null) {
    this.navigate(locationOnBoardingScreenContent, navOptions)
}

fun NavGraphBuilder.locationRouter(navController: NavController) {
    composable(route = locationOnBoardingScreenContent) {
        LocationOnBoardingScreenContent(navController=navController)
    }
}