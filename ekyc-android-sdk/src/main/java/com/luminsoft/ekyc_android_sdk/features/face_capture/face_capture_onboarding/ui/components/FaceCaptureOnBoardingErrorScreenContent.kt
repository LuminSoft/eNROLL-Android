package com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_onboarding.ui.components


import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.core.models.EKYCFailedModel
import com.luminsoft.ekyc_android_sdk.core.sdk.EkycSdk
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_navigation.faceCaptureBoardingPostScanScreenContent
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_navigation.faceCaptureOnBoardingErrorScreen
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingErrorScreen
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.ekyc_android_sdk.innovitices.activities.SmileLivenessActivity
import com.luminsoft.ekyc_android_sdk.innovitices.core.DotHelper
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.ui_components.components.ButtonView


@Composable
fun FaceCaptureOnBoardingErrorScreen(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel,
) {
    val rememberedViewModel = remember { onBoardingViewModel }
    val context = LocalContext.current
    val activity = context.findActivity()
    val errorMessage = onBoardingViewModel.errorMessage.collectAsState()

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val smileImageUri = it.data?.data
            if (smileImageUri != null) {
                try {
                    val smileImageBitmap =
                        DotHelper.getThumbnail(smileImageUri, activity)
                    rememberedViewModel.smileImage.value = smileImageBitmap
                    navController.navigate(faceCaptureBoardingPostScanScreenContent)
                } catch (e: Exception) {
                    onBoardingViewModel.errorMessage.value = e.message
                    onBoardingViewModel.scanType.value = ScanType.FRONT
                    navController.navigate(nationalIdOnBoardingErrorScreen)
                    println(e.message)
                }
            } else if (it.resultCode == 19 || it.resultCode == 8) {
                onBoardingViewModel.errorMessage.value =
                    context.getString(R.string.timeoutException)
                navController.navigate(faceCaptureOnBoardingErrorScreen)
            }
        }


    Surface(modifier = Modifier.fillMaxSize()) {
        Image(
            painterResource(R.drawable.blured_bg),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)

        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.25f))
            Image(
                painterResource(R.drawable.face_recognition_capture_error),
                contentDescription = "",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.fillMaxHeight(0.35f)
            )
            Spacer(modifier = Modifier.height(30.dp))
            errorMessage.value?.let { Text(text = it) }
            Spacer(modifier = Modifier.fillMaxHeight(0.35f))

            ButtonView(
                onClick = {
                    activity.finish()
                    EkycSdk.ekycCallback?.error(
                        EKYCFailedModel(
                            errorMessage.value!!, errorMessage
                        )
                    )
                }, title = stringResource(id = R.string.exit)
            )
            Spacer(modifier = Modifier.height(20.dp))

            ButtonView(
                onClick = {
                    val intent =
                        Intent(activity.applicationContext, SmileLivenessActivity::class.java)
                    startForResult.launch(intent)
                },
                textColor = MaterialTheme.colorScheme.primary,
                color = MaterialTheme.colorScheme.onPrimary,
                borderColor = MaterialTheme.colorScheme.primary,
                title = stringResource(id = R.string.reScan)
            )
        }

    }

}

