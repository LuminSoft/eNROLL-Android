
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update.ui.components.PhoneAuthUpdateScreenContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel

const val phoneAuthUpdateScreenContent = "phoneAuthUpdateScreenContent"

fun NavGraphBuilder.phoneAuthUpdateRouter(
    navController: NavController, updateViewModel: UpdateViewModel
) {
    composable(route = phoneAuthUpdateScreenContent) {
        PhoneAuthUpdateScreenContent(navController = navController, updateViewModel = updateViewModel)
    }
}