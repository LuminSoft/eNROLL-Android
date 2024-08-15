package com.luminsoft.enroll_sdk.features_update.email_update.email_navigation_update

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_update.email_update.email_update.ui.components.MultipleMailsUpdateScreenContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel

//const val mailsOnBoardingScreenContent = "mailsOnBoardingScreenContent"
const val multipleMailsUpdateScreenContent = "multipleMailsUpdateScreenContent"
//const val validateOtpMailsScreenContent = "validateOtpMailsScreenContent"


fun NavGraphBuilder.emailUpdateRouter(
    navController: NavController,
    updateViewModel: UpdateViewModel
) {
//    composable(route = mailsOnBoardingScreenContent) {
//        MailsOnBoardingScreenContent(
//            navController = navController,
//            updateViewModel = updateViewModel
//        )
//    }
    composable(route = multipleMailsUpdateScreenContent) {
        MultipleMailsUpdateScreenContent(
            navController = navController,
            updateViewModel = updateViewModel
        )
    }
//    composable(route = validateOtpMailsScreenContent) {
//        ValidateOtpMailsScreenContent(
//            navController = navController,
//            updateViewModel = updateViewModel,
//        )
//    }
}