
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_auth_update.device_id_auth_update.device_id_auth_update.ui.components.TestUpdateScreenContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel

const val checkDeviceIdAuthUpdateScreenContent = "checkDeviceIdAuthUpdateScreenContent"
const val testUpdateScreenContent = "testUpdateScreenContent"
fun NavGraphBuilder.checkDeviceIdAuthUpdateRouter(
    navController: NavController,
    updateViewModel: UpdateViewModel
) {
    composable(route = checkDeviceIdAuthUpdateScreenContent) {
        DeviceIdAuthUpdateScreenContent(
            navController = navController,
            updateViewModel = updateViewModel
        )
    }
    composable(route = testUpdateScreenContent) {
        TestUpdateScreenContent(
            navController = navController,
            updateViewModel = updateViewModel
        )
    }
}