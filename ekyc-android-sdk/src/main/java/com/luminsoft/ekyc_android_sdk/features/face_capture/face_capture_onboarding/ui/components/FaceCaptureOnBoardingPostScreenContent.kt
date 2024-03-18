package com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_onboarding.ui.components

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.model.content.CircleShape
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
import kotlin.math.roundToInt


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

//    GameCharacterMovement()
    MainContent(
        navController,
        onBoardingViewModel,
        activity,
        startForResult,
        faceCaptureOnBoardingPostScanViewModel!!
    )
}

@Composable
fun GameCharacterMovement() {

    val startPosition = Offset(50f, 100f)
    val endPosition = Offset(200f, 100f)
    val position = remember { Animatable(startPosition, Offset.VectorConverter) }

    val startPosition1 = Offset(350f, 100f)
    val position1 = remember { Animatable(startPosition1, Offset.VectorConverter) }

    LaunchedEffect(endPosition) {
        position.animateTo(
            targetValue = endPosition,
            animationSpec = keyframes {
                durationMillis = 5000
                //                controlPoint.value at 2500 // midway point controlled by the draggable control point
            }
        )
    }
    LaunchedEffect(endPosition) {
        position1.animateTo(
            targetValue = endPosition,
            animationSpec = keyframes {
                durationMillis = 5000
            }
        )
    }


    Box(modifier = Modifier.fillMaxSize()) {

        Row {
            Icon(
                Icons.Filled.Face, contentDescription = "Localized description", modifier = Modifier
                    .size(50.dp)
                    .offset(x = position.value.x.dp, y = position.value.y.dp)
            )
            Icon(
                Icons.Filled.Face, contentDescription = "Localized description", modifier = Modifier
                    .size(50.dp)
                    .offset(x = position1.value.x.dp, y = position1.value.y.dp)
            )
        }

    }


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

