package com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_navigation

import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth.ui.components.CheckIMEIAuthScreenContent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel

const val checkIMEIAuthScreenContent =
    "checkIMEIAuthScreenContent"

fun NavGraphBuilder.checkIMEIAuthRouter(
    navController: NavController,
    authViewModel: AuthViewModel

) {
    composable(route = checkIMEIAuthScreenContent) {
        CheckIMEIAuthScreenContent(
            navController = navController,
            authViewModel = authViewModel

        )
    }
}