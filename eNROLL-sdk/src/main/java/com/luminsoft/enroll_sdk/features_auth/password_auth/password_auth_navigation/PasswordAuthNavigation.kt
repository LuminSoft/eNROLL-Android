package com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth.ui.components.PasswordAuthScreenContent

const val passwordAuthScreenContent = "passwordAuthScreenContent"

fun NavGraphBuilder.passwordAuthRouter(navController: NavController) {
    composable(route = passwordAuthScreenContent) {
        PasswordAuthScreenContent(navController = navController)
    }
}