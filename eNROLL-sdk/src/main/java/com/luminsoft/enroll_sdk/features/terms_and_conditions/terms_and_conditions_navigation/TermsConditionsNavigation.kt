
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel


const val termsConditionsOnBoardingScreenContent = "termsConditionsOnBoardingScreenContent"

fun NavGraphBuilder.termsConditionsRouter(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel

) {
    composable(route = termsConditionsOnBoardingScreenContent) {
        TermsConditionsOnBoardingScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel

        )
    }
}