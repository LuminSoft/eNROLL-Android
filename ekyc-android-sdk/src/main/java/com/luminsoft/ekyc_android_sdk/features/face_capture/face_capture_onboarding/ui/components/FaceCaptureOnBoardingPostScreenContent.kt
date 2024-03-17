package com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_onboarding.ui.components

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
import androidx.compose.material3.MaterialTheme
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
import com.luminsoft.ekyc_android_sdk.core.failures.AuthFailure
import com.luminsoft.ekyc_android_sdk.core.models.EKYCFailedModel
import com.luminsoft.ekyc_android_sdk.core.sdk.EkycSdk
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_domain.usecases.FaceCaptureUseCase
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_domain.usecases.SelfieImageApproveUseCase
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_onboarding.view_model.FaceCaptureOnBoardingPostScanViewModel
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingErrorScreen
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingFrontConfirmationScreen
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.ekyc_android_sdk.innovitices.activities.DocumentActivity
import com.luminsoft.ekyc_android_sdk.innovitices.core.DotHelper
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.ui_components.components.BackGroundView
import com.luminsoft.ekyc_android_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.ekyc_android_sdk.ui_components.components.ButtonView
import com.luminsoft.ekyc_android_sdk.ui_components.components.DialogView
import com.luminsoft.ekyc_android_sdk.ui_components.components.SpinKitLoadingIndicator
import org.koin.compose.koinInject


@Composable
fun FaceCaptureBoardingPostScanScreenContent(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel

) {

    val rememberedViewModel = remember { onBoardingViewModel }
    val context = LocalContext.current
    val activity = context.findActivity()
    val document = onBoardingViewModel.smileImage.collectAsState()


    val faceCaptureUseCase = FaceCaptureUseCase(koinInject())
    val selfieImageApproveUseCase = SelfieImageApproveUseCase(koinInject())

    val faceCaptureOnBoardingPostScanViewModel =
        document.value?.let {
            remember {
                onBoardingViewModel.customerId.value?.let { it1 ->
                    FaceCaptureOnBoardingPostScanViewModel(
                        faceCaptureUseCase,
                        selfieImageApproveUseCase,
                        it,
                        it1
                    )
                }
            }
        }


    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val smileImageUri = it.data?.data
            if (smileImageUri != null) {
                try {
                    val smileImageBitmap =
                        DotHelper.getThumbnail(smileImageUri, activity)
                    rememberedViewModel.smileImage.value = smileImageBitmap
                    navController.navigate(nationalIdOnBoardingFrontConfirmationScreen)
                } catch (e: Exception) {
                    onBoardingViewModel.errorMessage.value = e.message
                    onBoardingViewModel.scanType.value = ScanType.FRONT
                    navController.navigate(nationalIdOnBoardingErrorScreen)
                    println(e.message)
                }
            }
        }

    MainContent(
        navController,
        onBoardingViewModel,
        activity,
        startForResult,
        faceCaptureOnBoardingPostScanViewModel!!
    )
}

@Composable
private fun MainContent(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel,
    activity: Activity,
    startForResult: ManagedActivityResultLauncher<Intent, ActivityResult>,
    faceCaptureOnBoardingPostScanViewModel: FaceCaptureOnBoardingPostScanViewModel
) {
    val faceCaptureViewModel = remember { faceCaptureOnBoardingPostScanViewModel }

    val loading = faceCaptureViewModel.loading.collectAsState()
    val uploadSelfieData = faceCaptureViewModel.uploadSelfieData.collectAsState()
    val failure = faceCaptureViewModel.failure.collectAsState()
    val selfieImageApproved = faceCaptureViewModel.selfieImageApproved.collectAsState()

    BackGroundView(navController = navController, showAppBar = false) {

        if (selfieImageApproved.value) {
            onBoardingViewModel.removeCurrentStep(2)
        }

        if (loading.value)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { SpinKitLoadingIndicator() }
        else if (!failure.value?.message.isNullOrEmpty()) {
            if (failure.value is AuthFailure) {
                failure.value?.let {
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            activity.finish()
                            EkycSdk.ekycCallback?.error(EKYCFailedModel(it.message, it))

                        },
                    )
                    {
                        activity.finish()
                        EkycSdk.ekycCallback?.error(EKYCFailedModel(it.message, it))

                    }
                }
            } else {
                failure.value?.let {
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.retry),
                        onPressedButton = {
                            val intent =
                                Intent(activity.applicationContext, DocumentActivity::class.java)
                            intent.putExtra("scanType", DocumentActivity().FRONT_SCAN)
                            intent.putExtra("localCode", EkycSdk.localizationCode.name)
                            startForResult.launch(intent)
                        },
                        secondButtonText = stringResource(id = R.string.exit),
                        onPressedSecondButton = {
                            activity.finish()
                            EkycSdk.ekycCallback?.error(EKYCFailedModel(it.message, it))

                        }
                    )
                    {
                        activity.finish()
                        EkycSdk.ekycCallback?.error(EKYCFailedModel(it.message, it))
                    }
                }
            }
        } else if (uploadSelfieData.value != null) {
            if (uploadSelfieData.value!!.photoMatched!!)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    ButtonView(
                        onClick = {
                            faceCaptureViewModel.callApproveSelfieImage()
                        },
                        textColor = MaterialTheme.colorScheme.primary,
                        color = MaterialTheme.colorScheme.onPrimary,
                        borderColor = MaterialTheme.colorScheme.primary,
                        title = stringResource(id = R.string.locationSuccessText)
                    )
                } else
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    ButtonView(
                        onClick = {
                            val intent =
                                Intent(activity.applicationContext, DocumentActivity::class.java)
                            intent.putExtra("scanType", DocumentActivity().FRONT_SCAN)
                            intent.putExtra("localCode", EkycSdk.localizationCode.name)
                            startForResult.launch(intent)
                        },
                        textColor = MaterialTheme.colorScheme.primary,
                        color = MaterialTheme.colorScheme.onPrimary,
                        borderColor = MaterialTheme.colorScheme.primary,
                        title = stringResource(id = R.string.initFail)
                    )
                }

        }
    }
}

