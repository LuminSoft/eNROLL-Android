package com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_navigation
import com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_onboarding.ui.components.UpdateNationalIdBackConfirmationScreen
import com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_onboarding.ui.components.UpdateNationalIdFrontConfirmationScreen
import UpdateNationalIdPreScanScreen
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_onboarding.ui.components.UpdateNationalIdErrorScreen
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel

const val updateNationalIdPreScanScreen = "com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_navigation.updateNationalIdPreScanScreen"
const val updateNationalIdFrontConfirmationScreen = "com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_navigation.updateNationalIdFrontConfirmationScreen"
const val updateNationalIdBackConfirmationScreen = "com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_navigation.updateNationalIdBackConfirmationScreen"
const val updateNationalIdErrorScreen = "com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_navigation.updateNationalIdErrorScreen"

fun NavGraphBuilder.updateNationalIdRouter(
    navController: NavController,
    updateViewModel: UpdateViewModel
) {

    composable(route = updateNationalIdPreScanScreen) {
        UpdateNationalIdPreScanScreen(
            navController = navController,
            updateViewModel = remember { updateViewModel }
        )
    }
    composable(route = updateNationalIdFrontConfirmationScreen) {
        UpdateNationalIdFrontConfirmationScreen(
            navController = navController,
            updateViewModel = updateViewModel
        )
    }

    composable(route = updateNationalIdBackConfirmationScreen) {
        UpdateNationalIdBackConfirmationScreen(
            navController = navController,
            updateViewModel = updateViewModel
        )
    }
    composable(route = updateNationalIdErrorScreen) {
        UpdateNationalIdErrorScreen(
            navController = navController,
            updateViewModel = updateViewModel
        )
    }
}