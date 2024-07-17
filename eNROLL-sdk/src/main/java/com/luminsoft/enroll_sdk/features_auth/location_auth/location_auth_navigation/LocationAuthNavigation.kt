package com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth.ui.components.LocationAuthScreenContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel

const val locationAuthScreenContent = "com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_navigation.locationAuthScreenContent"
fun NavGraphBuilder.locationAuthRouter(
    navController: NavController,
    authViewModel: AuthViewModel

) {
    composable(route = locationAuthScreenContent) {
        LocationAuthScreenContent(
            navController = navController,
            authViewModel = authViewModel

        )
    }
}