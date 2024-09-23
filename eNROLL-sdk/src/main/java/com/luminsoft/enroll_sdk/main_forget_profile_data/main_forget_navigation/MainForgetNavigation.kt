package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_navigation

import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.common.EnterNationalIdOrMRZScreenContent
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.common.VerifyPasswordScreenContent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.common.ForgetListScreenContent
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.common.SplashScreenForgetContent
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.main_forget.view_model.ForgetViewModel

const val splashScreenForgetContent = "splashScreenForgetContent"
const val forgetListScreenContent = "forgetListScreenContent"
const val enterNIDorMRZScreenContent = "enterNIDorMRZScreenContent"
const val verifyPasswordScreenContent = "verifyPasswordScreenContent"

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

    composable(route = enterNIDorMRZScreenContent) {
        EnterNationalIdOrMRZScreenContent(forgetViewModel, navController = navController)
    }

    composable(route = verifyPasswordScreenContent) {
        VerifyPasswordScreenContent(forgetViewModel, navController = navController)
    }

}