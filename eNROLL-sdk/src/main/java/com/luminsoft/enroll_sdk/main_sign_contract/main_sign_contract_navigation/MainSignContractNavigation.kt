package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_navigation

import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_presentation.common.SplashScreenSignContractContent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_presentation.main_sign_contract.view_model.SignContractViewModel

const val splashScreenSignContractContent = "splashScreenSignContractContent"

fun NavGraphBuilder.mainSignContractRouter(
    navController: NavController,
    authViewModel: SignContractViewModel
) {

    composable(route = splashScreenSignContractContent) {
        SplashScreenSignContractContent(authViewModel, navController = navController)
    }
}