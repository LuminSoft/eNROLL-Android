
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val electronicSignatureContent="electronicSignatureOnboardingScreenContent"
const val applyElectronicSignatureContent="applyElectronicSignatureOnboardingScreenContent"

fun NavGraphBuilder.electronicSignatureRouter(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {
    composable(route = electronicSignatureContent) {
        ElectronicSignatureOnBoardingScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel

        )
    }
    composable(route = applyElectronicSignatureContent) {
        ApplyForElectronicSignatureScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel

        )
    }
}