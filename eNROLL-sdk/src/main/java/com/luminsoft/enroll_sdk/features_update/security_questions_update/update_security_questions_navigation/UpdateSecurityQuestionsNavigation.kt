
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_update.security_questions_update.update_security_questions.ui.components.UpdateSecurityQuestionsScreenContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel

const val updateSecurityQuestionsScreenContent = "updateSecurityQuestionsScreenContent"


fun NavGraphBuilder.updateSecurityQuestionsRouter(
    navController: NavController,
    updateViewModel: UpdateViewModel
) {
    composable(route = updateSecurityQuestionsScreenContent) {
        UpdateSecurityQuestionsScreenContent(
            navController = navController,
           updateViewModel = updateViewModel
        )
    }
}