
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

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