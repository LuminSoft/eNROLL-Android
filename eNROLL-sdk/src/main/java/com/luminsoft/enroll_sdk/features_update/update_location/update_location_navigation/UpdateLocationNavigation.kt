package com.luminsoft.enroll_sdk.features_update.update_location.update_location_navigation

import com.luminsoft.enroll_sdk.features_update.update_location.update_location_presentation.ui.components.UpdateLocationScreenContent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val updateLocationScreenContent = "updateLocationScreenContent"


fun NavGraphBuilder.updateLocationRouter(
    navController: NavController
) {
    composable(route = updateLocationScreenContent) {
        UpdateLocationScreenContent(
            navController = navController
        )
    }
}