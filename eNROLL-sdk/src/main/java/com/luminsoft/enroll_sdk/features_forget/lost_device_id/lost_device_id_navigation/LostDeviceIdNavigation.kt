
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

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