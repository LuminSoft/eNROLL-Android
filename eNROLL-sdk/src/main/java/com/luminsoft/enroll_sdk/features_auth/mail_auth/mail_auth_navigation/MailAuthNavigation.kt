package com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth.ui.components.MailAuthScreenContent
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth.ui.components.PasswordAuthScreenContent

const val mailAuthScreenContent = "mailAuthScreenContent"

fun NavGraphBuilder.mailAuthRouter(navController: NavController) {
    composable(route = mailAuthScreenContent) {
        MailAuthScreenContent(navController = navController)
    }
}