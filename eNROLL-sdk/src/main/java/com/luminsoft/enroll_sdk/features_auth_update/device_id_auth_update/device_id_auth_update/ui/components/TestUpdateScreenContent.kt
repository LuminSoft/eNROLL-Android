
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import org.koin.compose.koinInject

@Composable
fun TestUpdateScreenContent(
    updateViewModel: UpdateViewModel,
    navController: NavController,
) {
    val context = LocalContext.current

    val checkDeviceIdAuthUpdateUseCase =
        CheckDeviceIdAuthUpdateUseCase(koinInject())

    val checkDeviceIdAuthUpdateViewModel =
        remember {
            DeviceIdAuthUpdateViewModel( checkDeviceIdAuthUseCase= checkDeviceIdAuthUpdateUseCase, context = context)
        }


    val activity = context.findActivity()
    val loading = checkDeviceIdAuthUpdateViewModel.loading.collectAsState()
    val failure = checkDeviceIdAuthUpdateViewModel.failure.collectAsState()
    val deviceIdChecked = checkDeviceIdAuthUpdateViewModel.deviceIdChecked.collectAsState()


    BackGroundView(navController = navController, showAppBar = false) {
        Text(text = "TestUpdateScreenContent")

    }
}
