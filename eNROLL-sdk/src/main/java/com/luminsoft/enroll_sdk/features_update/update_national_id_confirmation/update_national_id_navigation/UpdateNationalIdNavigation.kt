
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel

const val updateNationalIdPreScanScreen = "updateNationalIdPreScanScreen"
const val updateNationalIdFrontConfirmationScreen = "updateNationalIdFrontConfirmationScreen"
const val updateNationalIdBackConfirmationScreen = "updateNationalIdBackConfirmationScreen"
const val updateNationalIdErrorScreen = "updateNationalIdErrorScreen"

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