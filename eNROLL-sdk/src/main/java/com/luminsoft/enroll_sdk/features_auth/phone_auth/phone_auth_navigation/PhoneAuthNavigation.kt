package com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth.ui.components.PhoneAuthScreenContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel

const val phoneAuthScreenContent = "phoneAuthScreenContent"

fun NavGraphBuilder.phoneAuthRouter(
    navController: NavController, authViewModel: AuthViewModel
) {
    composable(route = phoneAuthScreenContent) {
        PhoneAuthScreenContent(navController = navController, authViewModel = authViewModel)
    }
}