package com.luminsoft.enroll_sdk.main_update.main_update_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.common.SplashScreenUpdateContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel

const val splashScreenUpdateContent = "splashScreenUpdateContent"

fun NavGraphBuilder.mainUpdateRouter(
    navController: NavController,
    updateViewModel: UpdateViewModel
) {

    composable(route = splashScreenUpdateContent) {
        SplashScreenUpdateContent(updateViewModel, navController = navController)
    }
}