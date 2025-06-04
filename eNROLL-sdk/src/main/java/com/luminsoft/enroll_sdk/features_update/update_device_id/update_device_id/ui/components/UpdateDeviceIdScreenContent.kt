package com.luminsoft.enroll_sdk.features_update.update_device_id.update_device_id.ui.components

import UpdateDeviceIdViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.EnrollSuccessModel
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import org.koin.androidx.compose.koinViewModel

@Composable
fun UpdateDeviceIdScreenContent(
    navController: NavController
) {
    val context = LocalContext.current
    val updateDeviceIdViewModel: UpdateDeviceIdViewModel = koinViewModel()
    val activity = context.findActivity()
    val loading = updateDeviceIdViewModel.loading.collectAsState()
    val failure = updateDeviceIdViewModel.failure.collectAsState()
    val deviceUpdated = updateDeviceIdViewModel.deviceIdUpdated.collectAsState()

    BackGroundView(navController = navController, showAppBar = false) {
        if (deviceUpdated.value) {
            DialogView(
                bottomSheetStatus = BottomSheetStatus.SUCCESS,
                text = stringResource(id = R.string.successfulUpdate),
                buttonText = stringResource(id = R.string.continue_to_next),
                onPressedButton = {
                    activity.finish()
                    EnrollSDK.enrollCallback?.success(
                        EnrollSuccessModel(
                            activity.getString(R.string.successfulAuthentication)
                        )
                    )
                },
            )
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
                    DialogView(bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.retry),
                        onPressedButton = {
                            updateDeviceIdViewModel.callUpdateDeviceId()
                        },
                        secondButtonText = stringResource(id = R.string.exit),
                        onPressedSecondButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                        }) {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                    }
                }
            }
        }

    }
}
