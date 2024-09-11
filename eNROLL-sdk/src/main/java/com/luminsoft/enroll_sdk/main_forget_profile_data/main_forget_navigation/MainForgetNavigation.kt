package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_navigation

import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.common.SplashScreenForgetContent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.main_forget.view_model.ForgetViewModel
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.common.ForgetListScreenContent

const val splashScreenForgetContent = "splashScreenForgetContent"
const val forgetListScreenContent = "forgetListScreenContent"

fun NavGraphBuilder.mainForgetRouter(
    navController: NavController,
    forgetViewModel: ForgetViewModel
) {

    composable(route = splashScreenForgetContent) {
        SplashScreenForgetContent(forgetViewModel, navController = navController)
    }
    composable(route = forgetListScreenContent) {
        ForgetListScreenContent(forgetViewModel, navController = navController)
    }
}