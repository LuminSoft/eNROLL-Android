package com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth.ui.components.CheckExpiryDateAuthScreenContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel

const val checkExpiryDateAuthScreenContent = "checkExpiryDateAuthScreenContent"
fun NavGraphBuilder.checkExpiryDateAuthRouter(
    navController: NavController,
    authViewModel: AuthViewModel

) {
    composable(route = checkExpiryDateAuthScreenContent) {
        CheckExpiryDateAuthScreenContent(
            navController = navController,
            authViewModel = authViewModel

        )
    }
}