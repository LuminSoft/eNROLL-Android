
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_update.update_device_id.update_device_id.ui.components.UpdateDeviceIdScreenContent

const val updateDeviceIdScreenContent =
    "updateDeviceIdScreenContent"

fun NavGraphBuilder.updateDeviceIdRouter(
    navController: NavController,
) {
    composable(route = updateDeviceIdScreenContent) {
        UpdateDeviceIdScreenContent(
            navController = navController,
        )
    }
}