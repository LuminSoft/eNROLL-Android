package com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_onboarding.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_domain.usecases.CheckNtraUseCase
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_onboarding.view_model.CheckNtraOnBoardingViewModel
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.EkycStepType
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import org.koin.compose.koinInject


@Composable
fun CheckNtraOnBoardingScreenContent(
    onBoardingViewModel: OnBoardingViewModel,
    navController: NavController,
) {

    val checkNtraUseCase =
        CheckNtraUseCase(koinInject())


    val checkNtraOnBoardingViewModel =
        remember {
            CheckNtraOnBoardingViewModel(
                checkNtraUseCase = checkNtraUseCase
            )
        }

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = checkNtraOnBoardingViewModel.loading.collectAsState()
    val failure = checkNtraOnBoardingViewModel.failure.collectAsState()
    val ntraChecked = checkNtraOnBoardingViewModel.ntraChecked.collectAsState()
    val ntraSucceeded = checkNtraOnBoardingViewModel.ntraSucceeded.collectAsState()


    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogStatus by remember { mutableStateOf(BottomSheetStatus.SUCCESS) }
    var dialogButtonText by remember { mutableStateOf("") }
    var dialogOnPressButton: (() -> Unit)? by remember { mutableStateOf(null) }




    LaunchedEffect(ntraChecked.value) {
        if (ntraSucceeded.value != null && ntraSucceeded.value!!) {
            onBoardingViewModel.removeCurrentStep(EkycStepType.NtraCheck.getStepId())
        } else if (ntraSucceeded.value != null && !ntraSucceeded.value!!) {
            dialogMessage = context.getString(R.string.ntra_check_failed)
            dialogButtonText = context.getString(R.string.exit)
            dialogStatus = BottomSheetStatus.ERROR
            dialogOnPressButton = {
                activity.finish()
                EnrollSDK.enrollCallback?.error(
                    EnrollFailedModel(
                        R.string.ntra_check_failed.toString(),
                        R.string.ntra_check_failed
                    )
                )
            }
            showDialog = true
        }
    }

    BackGroundView(navController = navController, showAppBar = true) {

        if (showDialog) {
            dialogOnPressButton?.let {
                DialogView(
                    bottomSheetStatus = dialogStatus,
                    text = dialogMessage,
                    buttonText = dialogButtonText,
                    onPressedButton = it
                )
            }
        }
        if (loading.value) LoadingView()
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
                            checkNtraOnBoardingViewModel.callCheckNtra()
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
