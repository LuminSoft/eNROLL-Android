
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_forget.lost_device_id.lost_device_id_presentation.ui.components.LostDeviceIdScreenContent

const val lostDeviceIdScreenContent =
    "lostDeviceIdScreenContent"

fun NavGraphBuilder.lostDeviceIdRouter(
    navController: NavController,
) {
    composable(route = lostDeviceIdScreenContent) {
        LostDeviceIdScreenContent(
            navController = navController,
        )
    }
}