package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_navigation_update

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_update.ui.components.MultiplePhonesUpdateScreenContent
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_update.ui.components.PhonesUpdateScreenContent
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_update.ui.components.ValidateOtpPhonesUpdateScreenContent

import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel

const val phonesUpdateScreenContent = "phonesUpdateScreenContent"
const val multiplePhonesUpdateScreenContent = "multiplePhonesUpdateScreenContent"
const val validateOtpPhonesUpdateScreenContent = "validateOtpPhonesUpdateScreenContent"


fun NavGraphBuilder.phoneUpdateRouter(
    navController: NavController,
    updateViewModel: UpdateViewModel
) {
    composable(route = phonesUpdateScreenContent) {
        PhonesUpdateScreenContent(
            navController = navController,
            updateViewModel = updateViewModel
        )
    }
    composable(route = multiplePhonesUpdateScreenContent) {
        MultiplePhonesUpdateScreenContent(
            navController = navController,
            updateViewModel = updateViewModel
        )
    }
    composable(route = validateOtpPhonesUpdateScreenContent) {
        ValidateOtpPhonesUpdateScreenContent(
            navController = navController,
            updateViewModel = updateViewModel,
        )
    }
}