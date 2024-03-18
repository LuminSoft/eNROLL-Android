package com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_onboarding.ui.components

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.core.failures.AuthFailure
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
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
    val faceImage = remember { onBoardingViewModel.faceImage.value }
    val smileImage = remember { onBoardingViewModel.smileImage.value }

    val loading = faceCaptureViewModel.loading.collectAsState()
    val uploadSelfieData = faceCaptureViewModel.uploadSelfieData.collectAsState()
    val failure = faceCaptureViewModel.failure.collectAsState()
    val selfieImageApproved = faceCaptureViewModel.selfieImageApproved.collectAsState()
    val startPosition = Offset(400f, 100f)
    val endPosition = Offset(0f, 100f)
    val endPosition1 = Offset(-0f, 100f)
    val position = remember { Animatable(startPosition, Offset.VectorConverter) }
    val startPosition1 = Offset(-400f, 100f)
    val position1 = remember { Animatable(startPosition1, Offset.VectorConverter) }
    LaunchedEffect(endPosition) {
        position.animateTo(
            targetValue = endPosition,
            animationSpec = tween(
                durationMillis = 6000,
                easing = LinearOutSlowInEasing
            )
        )
    }
    LaunchedEffect(endPosition1) {
        position1.animateTo(
            targetValue = endPosition1,
            animationSpec = tween(
                durationMillis = 6000,
                easing = LinearOutSlowInEasing
            )
        )
    }

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
            FailureExtract(failure, activity, startForResult)
        } else if (uploadSelfieData.value != null) {
            if (uploadSelfieData.value!!.photoMatched!!)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.fillMaxHeight(0.2f))

                    AnimationExtracted(faceImage, position, smileImage, position1)
                    Spacer(modifier = Modifier.fillMaxHeight(0.3f))
                    androidx.compose.material3.Text(
                        text = stringResource(id = R.string.ekycSuccessfullyDone),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.fillMaxHeight(0.2f))

                    ButtonView(
                        onClick = {
                            faceCaptureViewModel.callApproveSelfieImage()
                        },
                        title = stringResource(id = R.string.continue_to_next)
                    )
                }
            else
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

@Composable
private fun AnimationExtracted(
    faceImage: Bitmap?,
    position: Animatable<Offset, AnimationVector2D>,
    smileImage: Bitmap?,
    position1: Animatable<Offset, AnimationVector2D>
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box {
                Image(
                    bitmap = faceImage!!.asImageBitmap(),
                    contentDescription = "some useful description",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .offset(x = position.value.x.dp, y = position.value.y.dp)
                        .border(2.dp, Color.Blue, shape = CircleShape)
                        .clip(CircleShape)
                )
                Image(
                    bitmap = smileImage!!.asImageBitmap(),
                    contentDescription = "some useful description",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .offset(x = position1.value.x.dp, y = position1.value.y.dp)
                        .border(2.dp, Color.Blue, shape = CircleShape)
                        .clip(CircleShape)
                )
            }
        }

    }
}

@Composable
private fun FailureExtract(
    failure: State<SdkFailure?>,
    activity: Activity,
    startForResult: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
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
}

