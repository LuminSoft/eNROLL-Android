
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.innovitices.activities.DocumentActivity
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.EnrollItemView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun UpdatePassportPreScanScreen(
    navController: NavController,
    updateViewModel: UpdateViewModel
) {

    val rememberedViewModel = remember { updateViewModel }
    val context = LocalContext.current
    val activity=context.findActivity()
    val isLoading by rememberedViewModel.preScanLoading.collectAsState()


    val startPassportForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            rememberedViewModel.enableLoading()
            val documentFrontUri = it.data?.data
            if (documentFrontUri != null) {
                try {
                    val facialDocumentModel =
                        DotHelper.documentNonFacial(documentFrontUri, activity)
                    rememberedViewModel.passportImage.value =
                        facialDocumentModel.documentImageBase64
                    navController.navigate(updatePassportConfirmationScreen)
                } catch (e: Exception) {
                    rememberedViewModel.disablePreScanLoading()
                    updateViewModel.disableLoading()
                    updateViewModel.errorMessage.value = e.message
                    navController.navigate(updatePassportErrorScreen)
                    println(e.message)
                }
            } else if (it.resultCode == 19 || it.resultCode == 8) {
                rememberedViewModel.disablePreScanLoading()
                updateViewModel.disableLoading()
                updateViewModel.errorMessage.value =
                    context.getString(R.string.timeoutException)
                navController.navigate(updatePassportErrorScreen)
            }
        }
    if (isLoading){
        BackGroundView(navController = navController, showAppBar = true) {
            LoadingView()
        }
    }
    else{
        PassportOnly(activity, startPassportForResult, rememberedViewModel)

//    BackGroundView(navController = navController, showAppBar = false) {
//        PassportOnly(activity, startPassportForResult, rememberedViewModel)

        /*       if (loading.value) {
                   Column(
                       modifier = Modifier.fillMaxSize(),
                       verticalArrangement = Arrangement.Center,
                       horizontalAlignment = Alignment.CenterHorizontally
                   ) { SpinKitLoadingIndicator() }
               } else
               {
                   PassportOnly(activity, startPassportForResult, rememberedViewModel)

               }*/

    }

}


@Composable
private fun PassportOnly(
    activity: Activity,
    startForResult: ManagedActivityResultLauncher<Intent, ActivityResult>,
    rememberedViewModel: UpdateViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        EnrollItemView(
            listOf(R.drawable.step_01_passport_1, R.drawable.step_01_passport_2, R.drawable.step_01_passport_3)
            , R.string.passportPreScanContent)
        ButtonView(
            onClick = {
                rememberedViewModel.enableLoading()
                rememberedViewModel.enablePreScanLoading()
                val intent = Intent(activity.applicationContext, DocumentActivity::class.java)
                intent.putExtra("scanType", DocumentActivity().passportScan)
                intent.putExtra("localCode", EnrollSDK.localizationCode.name)

                startForResult.launch(intent)
            },
            stringResource(id = R.string.start),
        )
        Spacer(
            modifier = Modifier
                .safeContentPadding()
                .height(10.dp)
        )
    }
}


