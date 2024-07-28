
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val checkAmlOnBoardingScreenContent="checkAmlOnBoardingScreenContent"

fun NavGraphBuilder.checkAmlRouter(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel

) {
    composable(route = checkAmlOnBoardingScreenContent) {
        CheckAmlOnBoardingScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel

        )
    }
}