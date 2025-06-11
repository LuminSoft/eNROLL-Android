package com.luminsoft.enroll_sdk.features_auth_update.face_capture_auth_update.face_capture_auth_update.ui.components

import FaceCaptureAuthUpdateUseCase
import FaceCaptureAuthUpdateViewModel
import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import org.koin.compose.koinInject


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FaceCaptureAuthUpdatePostScanScreenContent(
    navController: NavController,
    updateViewModel: UpdateViewModel
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val document = updateViewModel.smileImage.collectAsState()


    val faceCaptureUseCase = FaceCaptureAuthUpdateUseCase(koinInject())

    val faceCaptureAuthUpdateViewModel =
        document.value?.let {
            remember {
                FaceCaptureAuthUpdateViewModel(
                    faceCaptureUseCase,
                    it,
                )
            }

        }
    faceCaptureAuthUpdateViewModel!!.setStepUpdateId(updateViewModel.updateStepId.value!!)


    MainContent(
        navController,
        updateViewModel,
        activity,
        faceCaptureAuthUpdateViewModel
    )
}


@Composable
private fun MainContent(
    navController: NavController,
    updateViewModel: UpdateViewModel,
    activity: Activity,
    faceCaptureAuthUpdateViewModel: FaceCaptureAuthUpdateViewModel
) {

    val faceCaptureViewModel = remember { faceCaptureAuthUpdateViewModel }


    val loading = faceCaptureViewModel.loading.collectAsState()
    val failure = faceCaptureViewModel.failure.collectAsState()
    val selfieImageApproved = faceCaptureViewModel.selfieImageApproved.collectAsState()

    BackGroundView(navController = navController, showAppBar = false) {

        LaunchedEffect(selfieImageApproved.value) {
            if (selfieImageApproved.value) {
                updateViewModel.navigateToUpdateAfterAuthStep()
            }
        }
        if (loading.value)
            LoadingView()
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
                    ) {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                    }
                }
            }
        }
    }


}
