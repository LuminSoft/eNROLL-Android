import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.EkycStepType
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import org.koin.compose.koinInject


@Composable
fun CheckAmlOnBoardingScreenContent(
    onBoardingViewModel: OnBoardingViewModel,
    navController: NavController,
) {

    val checkAmlUseCase=
        CheckAmlUseCase(koinInject())



    val checkAmlOnBoardingViewModel =
        remember {
            CheckAmlOnBoardingViewModel(
                checkAmlUseCase = checkAmlUseCase
            )
        }

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = checkAmlOnBoardingViewModel.loading.collectAsState()
    val failure = checkAmlOnBoardingViewModel.failure.collectAsState()
    val amlChecked = checkAmlOnBoardingViewModel.amlChecked.collectAsState()


    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogStatus by remember { mutableStateOf(BottomSheetStatus.SUCCESS) }
    var dialogButtonText by remember { mutableStateOf("") }
    var dialogOnPressButton: (() -> Unit)? by remember { mutableStateOf(null) }




    LaunchedEffect(amlChecked.value) {
        if (amlChecked.value) {
            val isEmpty = onBoardingViewModel.removeCurrentStep(EkycStepType.AmlCheck.getStepId())
            if (isEmpty) {
                dialogMessage = context.getString(R.string.successfulRegistration)
                dialogButtonText = context.getString(R.string.continue_to_next)
                dialogStatus = BottomSheetStatus.SUCCESS
                dialogOnPressButton = {
                    activity.finish()
                    EnrollSDK.enrollCallback?.error(
                        EnrollFailedModel(
                            context.getString(R.string.successfulRegistration),
                            context.getString(R.string.successfulRegistration)
                        )
                    )
                }
                showDialog = true
            }
        }
    }


    BackGroundView(navController = navController, showAppBar = true) {

        if (showDialog) {
            dialogOnPressButton?.let {
                DialogView(
                    bottomSheetStatus = dialogStatus,
                    text = dialogMessage,
                    buttonText = dialogButtonText,
                    onPressedButton = it
                )
            }
        }
        if (loading.value) LoadingView()
        else if (!failure.value?.message.isNullOrEmpty()) {
            if (failure.value is AuthFailure) {
                failure.value?.let {
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                        },
                    ) {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                    }
                }
            } else {
                failure.value?.let {
                    DialogView(bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.retry),
                        onPressedButton = {
                            checkAmlOnBoardingViewModel.callCheckAml()
                        },
                        secondButtonText = stringResource(id = R.string.exit),
                        onPressedSecondButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                        }) {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                    }
                }
            }
        }

    }

}