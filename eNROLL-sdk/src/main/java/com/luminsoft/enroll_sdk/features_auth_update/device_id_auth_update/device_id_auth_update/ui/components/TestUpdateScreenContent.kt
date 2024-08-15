
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import org.koin.compose.koinInject

@Composable
fun TestUpdateScreenContent(
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


    val activity = context.findActivity()
    val loading = checkDeviceIdAuthUpdateViewModel.loading.collectAsState()
    val failure = checkDeviceIdAuthUpdateViewModel.failure.collectAsState()


    BackGroundView(navController = navController, showAppBar = true) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp), // Adjust horizontal padding as needed
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Adjust vertical spacing as needed
            ) {
                Text(
                    text = updateViewModel.convertStepUpdateIdToTitle()
                )

                Spacer(modifier = Modifier.height(48.dp))
                ButtonView(
                    color = MaterialTheme.appColors.errorColor,
                    onClick = {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(
                            EnrollFailedModel(
                                "Update Failed", "Update Failed"
                            )
                        )
                    }, title = stringResource(id = R.string.exit)
                )

            }
        }




        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = updateViewModel.convertStepUpdateIdToTitle(),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            ButtonView(
                onClick = {
                    activity.finish()
                    EnrollSDK.enrollCallback?.error(
                        EnrollFailedModel(
                            "Update Failed", "Update Failed"
                        )
                    )
                }, title = stringResource(id = R.string.exit)
            )
        }

    }
}
