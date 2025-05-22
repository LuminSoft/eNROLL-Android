package com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth.ui.components

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_navigation.faceCaptureAuthErrorScreen
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_navigation.faceCaptureAuthPostScanScreenContent
import com.luminsoft.enroll_sdk.innovitices.activities.SmileLivenessActivity
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.EnrollItemView


@Composable
fun FaceCaptureAuthPreScanScreenContent(
    navController: NavController,
    authViewModel: AuthViewModel

) {

    val rememberedViewModel = remember { authViewModel }
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
                    navController.navigate(faceCaptureAuthPostScanScreenContent)
                } catch (e: Exception) {
                    authViewModel.disableLoading()
                    authViewModel.errorMessage.value = e.message
                    authViewModel.scanType.value = ScanType.FRONT
                    navController.navigate(faceCaptureAuthErrorScreen)
                    println(e.message)
                }
            } else if (it.resultCode == 19 || it.resultCode == 8) {
                authViewModel.errorMessage.value =
                    context.getString(R.string.timeoutException)
                navController.navigate(faceCaptureAuthErrorScreen)
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
                listOf(R.drawable.step_02_smile_liveness_1, R.drawable.step_02_smile_liveness_2, R.drawable.step_02_smile_liveness_3)
                , R.string.facePreCapContent)
            ButtonView(
                onClick = {
                    val intent =
                        Intent(activity.applicationContext, SmileLivenessActivity::class.java)
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
}
