
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features_auth_update.device_id_auth_update.device_id_auth_update_domain.usecases.CheckDeviceIdAuthUpdateUseCase
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import org.koin.compose.koinInject

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DeviceIdAuthUpdateScreenContent(
    updateViewModel: UpdateViewModel,
    navController: NavController,
) {
    val context = LocalContext.current

    val checkDeviceIdAuthUpdateUseCase =
        CheckDeviceIdAuthUpdateUseCase(koinInject())

    val checkDeviceIdAuthUpdateViewModel =
        remember {
            DeviceIdAuthUpdateViewModel( checkDeviceIdAuthUseCase= checkDeviceIdAuthUpdateUseCase, context = context)
        }

    checkDeviceIdAuthUpdateViewModel.setStepUpdateId(updateViewModel.updateStepId.value!!)

    val activity = context.findActivity()
    val loading = checkDeviceIdAuthUpdateViewModel.loading.collectAsState()
    val failure = checkDeviceIdAuthUpdateViewModel.failure.collectAsState()
    val deviceIdChecked = checkDeviceIdAuthUpdateViewModel.deviceIdChecked.collectAsState()


    BackGroundView(navController = navController, showAppBar = false) {

        if (deviceIdChecked.value) {
            updateViewModel.navigateToUpdateAfterAuthStep()
        } else if (loading.value) LoadingView()
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
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                        },
                    )
                }
            }
        }

    }
}
