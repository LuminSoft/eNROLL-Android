package com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract.ui.components.SignContractScreenContent
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_presentation.main_sign_contract.view_model.SignContractViewModel

const val signContractScreenContent = "signContractScreenContent"

fun NavGraphBuilder.signContractRouter(
    navController: NavController, signContractViewModel: SignContractViewModel
) {
    composable(route = signContractScreenContent) {
        SignContractScreenContent(
            navController = navController,
            signContractViewModel = signContractViewModel
        )
    }
}