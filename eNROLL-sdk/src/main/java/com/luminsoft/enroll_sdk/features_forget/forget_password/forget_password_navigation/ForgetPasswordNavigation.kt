
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.main_forget.view_model.ForgetViewModel

const val forgetPasswordValidateOtpScreenContent = "validateOtpScreenContent"
const val forgetPasswordScreenContent = "forgetPasswordScreenContent"

fun NavGraphBuilder.forgetPasswordRouter(
    navController: NavController, forgetViewModel: ForgetViewModel
) {

    composable(route = forgetPasswordValidateOtpScreenContent) {
        ForgetPasswordAuthScreenContent(navController = navController, forgetViewModel = forgetViewModel)
    }

    composable(route = forgetPasswordScreenContent) {
        ForgetPasswordScreenContent(navController = navController, forgetViewModel = forgetViewModel)
    }

}