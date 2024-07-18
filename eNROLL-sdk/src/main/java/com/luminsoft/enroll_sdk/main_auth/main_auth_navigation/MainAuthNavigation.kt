package com.luminsoft.enroll_sdk.main_auth.main_auth_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.common.SplashScreenAuthContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel

const val splashScreenAuthContent = "splashScreenAuthContent"

fun NavGraphBuilder.mainAuthRouter(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    composable(route = splashScreenAuthContent) {
        SplashScreenAuthContent(authViewModel, navController = navController)
    }
}