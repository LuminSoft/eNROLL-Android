
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_auth_update.password_auth_update.password_auth_update.ui.components.PasswordAuthUpdateScreenContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel

const val passwordAuthUpdateScreenContent = "passwordAuthUpdateScreenContent"

fun NavGraphBuilder.passwordAuthUpdateRouter(navController: NavController, updateViewModel: UpdateViewModel) {
    composable(route = passwordAuthUpdateScreenContent) {
        PasswordAuthUpdateScreenContent(navController = navController, updateViewModel = updateViewModel)
    }
}