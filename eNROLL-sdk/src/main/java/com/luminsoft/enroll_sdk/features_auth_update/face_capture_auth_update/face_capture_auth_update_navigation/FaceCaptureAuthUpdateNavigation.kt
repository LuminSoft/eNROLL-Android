
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_auth_update.face_capture_auth_update.face_capture_auth_update.ui.components.FaceCaptureAuthUpdatePostScanScreenContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel

const val faceCaptureAuthUpdatePreScanScreenContent = "faceCaptureAuthUpdatePreScanScreenContent"
const val faceCaptureAuthUpdatePostScanScreenContent = "faceCaptureAuthUpdatePostScanScreenContent"
const val faceCaptureAuthUpdateErrorScreen = "faceCaptureAuthUpdateErrorScreen"

fun NavGraphBuilder.faceCaptureAuthUpdateRouter(
    navController: NavController, updateViewModel: UpdateViewModel
) {
    composable(route = faceCaptureAuthUpdatePreScanScreenContent) {
        FaceCaptureAuthUpdatePreScanScreenContent(
            navController = navController,
            updateViewModel = updateViewModel
        )
    }
    composable(route = faceCaptureAuthUpdatePostScanScreenContent) {
        FaceCaptureAuthUpdatePostScanScreenContent(
            navController = navController,
            updateViewModel = updateViewModel
        )
    }

    composable(route = faceCaptureAuthUpdateErrorScreen) {
        FaceCaptureAuthUpdateErrorScreen(
            navController = navController,
            updateViewModel = updateViewModel
        )
    }

}
