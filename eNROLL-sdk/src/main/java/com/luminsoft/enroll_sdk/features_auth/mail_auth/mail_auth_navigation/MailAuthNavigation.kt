package com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth.ui.components.MailAuthScreenContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel

const val mailAuthScreenContent = "mailAuthScreenContent"

fun NavGraphBuilder.mailAuthRouter(
    navController: NavController, authViewModel: AuthViewModel
) {
    composable(route = mailAuthScreenContent) {
        MailAuthScreenContent(navController = navController, authViewModel = authViewModel)
    }
}