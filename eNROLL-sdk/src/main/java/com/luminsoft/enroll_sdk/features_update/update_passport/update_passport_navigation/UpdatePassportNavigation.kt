
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_update.update_passport.update_passport_presentation.ui.components.UpdatePassportConfirmationScreen
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel

const val updatePassportPreScanScreen = "updatePassportPreScanScreen"
const val updatePassportConfirmationScreen = "updatePassportConfirmationScreen"
const val updatePassportErrorScreen = "updatePassportErrorScreen"

fun NavGraphBuilder.updatePassportRouter(
    navController: NavController,
    updateViewModel: UpdateViewModel
) {

    composable(route = updatePassportPreScanScreen) {
        UpdatePassportPreScanScreen(
            navController = navController,
            updateViewModel = remember { updateViewModel }
        )
    }

    composable(route = updatePassportConfirmationScreen) {
        UpdatePassportConfirmationScreen(
            navController = navController,
            updateViewModel = updateViewModel
        )
    }


    composable(route = updatePassportErrorScreen) {
        UpdatePassportErrorScreen(
            navController = navController,
            updateViewModel= updateViewModel
        )
    }
}