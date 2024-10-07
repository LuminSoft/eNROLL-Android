import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.main_forget.view_model.ForgetViewModel

const val forgetLocationScreenContent = "forgetLocationScreenContent"


fun NavGraphBuilder.forgetLocationRouter(
    navController: NavController,
    forgetViewModel: ForgetViewModel
) {
    composable(route = forgetLocationScreenContent) {
        ForgetLocationScreenContent(
            navController = navController,
            forgetViewModel = forgetViewModel

        )
    }
}