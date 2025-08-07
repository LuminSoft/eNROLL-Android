
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.innovitices.activities.SmileLivenessActivity
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.EnrollItemView
import com.luminsoft.enroll_sdk.ui_components.theme.appColors


@Composable
fun FaceCaptureAuthUpdatePreScanScreenContent(
    navController: NavController,
    updateViewModel: UpdateViewModel
) {

    val rememberedViewModel = remember { updateViewModel }
    val context = LocalContext.current
    val activity = context.findActivity()

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val smileImageUri = it.data?.data
            if (smileImageUri != null) {
                try {
                    val smileImageBitmap =
                        DotHelper.getThumbnail(smileImageUri, activity)
                    rememberedViewModel.smileImage.value = smileImageBitmap
                    navController.navigate(faceCaptureAuthUpdatePostScanScreenContent)
                } catch (e: Exception) {
                    updateViewModel.disableLoading()
                    updateViewModel.errorMessage.value = e.message
                    updateViewModel.scanType.value = UpdateScanType.FRONT
                    navController.navigate(faceCaptureAuthUpdateErrorScreen)
                    println(e.message)
                }
            } else if (it.resultCode == 19 || it.resultCode == 8) {
                updateViewModel.errorMessage.value =
                    context.getString(R.string.timeoutException)
                navController.navigate(faceCaptureAuthUpdateErrorScreen)
            }
        }

    BackGroundView(navController = navController, showAppBar = false) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            EnrollItemView(
                listOf( R.drawable.step_02_smile_liveness_1, R.drawable.step_02_smile_liveness_2, R.drawable.step_02_smile_liveness_3)
               , R.string.facePreCapContent)

            Column {
                ButtonView(
                    onClick = {
                        val intent =
                            Intent(activity.applicationContext, SmileLivenessActivity::class.java)
                        startForResult.launch(intent)
                    },
                    stringResource(id = R.string.start),
                )
                Spacer(modifier = Modifier.height(10.dp))

                ButtonView(
                    onClick = {
                        navController.popBackStack()
                    },
                    stringResource(id = R.string.cancel),
                    color = MaterialTheme.appColors.backGround,
                    borderColor = MaterialTheme.appColors.primary,
                    textColor = MaterialTheme.appColors.primary,
                ) }

            Spacer(
                modifier = Modifier
                    .safeContentPadding()
                    .height(10.dp)
            )
        }
    }
}
