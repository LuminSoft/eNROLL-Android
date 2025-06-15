
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_auth.security_question_auth.security_question_auth.ui.components.SecurityQuestionAuthScreenContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel

const val securityQuestionAuthScreenContent = "securityQuestionAuthScreenContent"

fun NavGraphBuilder.securityQuestionAuthRouter(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    composable(route = securityQuestionAuthScreenContent) {
        SecurityQuestionAuthScreenContent(
            navController = navController,
            authViewModel= authViewModel
        )
    }
}