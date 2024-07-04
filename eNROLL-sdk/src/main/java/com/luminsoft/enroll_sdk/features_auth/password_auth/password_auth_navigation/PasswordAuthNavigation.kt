package com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth.ui.components.PasswordAuthScreenContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel

const val passwordAuthScreenContent = "passwordAuthScreenContent"

fun NavGraphBuilder.passwordAuthRouter(navController: NavController, authViewModel: AuthViewModel) {
    composable(route = passwordAuthScreenContent) {
        PasswordAuthScreenContent(navController = navController, authViewModel = authViewModel)
    }
}