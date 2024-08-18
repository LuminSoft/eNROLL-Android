package com.luminsoft.enroll_sdk.features_update.email_update.email_navigation_update

import com.luminsoft.enroll_sdk.features_update.email_update.email_update.ui.components.MailsUpdateScreenContent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_update.email_update.email_update.ui.components.MultipleMailsUpdateScreenContent
import com.luminsoft.enroll_sdk.features_update.email_update.email_update.ui.components.ValidateOtpMailsUpdateScreenContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel

const val mailsUpdateScreenContent = "mailsUpdateScreenContent"
const val multipleMailsUpdateScreenContent = "multipleMailsUpdateScreenContent"
const val validateOtpMailsUpdateScreenContent = "validateOtpMailsUpdateScreenContent"


fun NavGraphBuilder.emailUpdateRouter(
    navController: NavController,
    updateViewModel: UpdateViewModel
) {
    composable(route = mailsUpdateScreenContent) {
        MailsUpdateScreenContent(
            navController = navController,
            updateViewModel = updateViewModel
        )
    }
    composable(route = multipleMailsUpdateScreenContent) {
        MultipleMailsUpdateScreenContent(
            navController = navController,
            updateViewModel = updateViewModel
        )
    }
    composable(route = validateOtpMailsUpdateScreenContent) {
        ValidateOtpMailsUpdateScreenContent(
            navController = navController,
            updateViewModel = updateViewModel,
        )
    }
}