package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_navigation

import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_presentation.main_sign_contract.view_model.SignContractViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra.ui.components.PhoneLowRiskFRAScreenContent

const val phoneScreenContent =
    "phoneScreenContent"

fun NavGraphBuilder.lowRiskFRARouter(
    navController: NavController, signContractViewModel: SignContractViewModel
) {
    composable(route = phoneScreenContent) {
        PhoneLowRiskFRAScreenContent(
            navController = navController,
            signContractViewModel = signContractViewModel
        )
    }
}