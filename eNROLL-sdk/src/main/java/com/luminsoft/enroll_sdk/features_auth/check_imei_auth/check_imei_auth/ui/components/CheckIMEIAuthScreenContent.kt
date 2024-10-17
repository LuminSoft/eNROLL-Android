package com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth.ui.components

import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth.view_model.CheckIMEIAuthViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.models.EnrollSuccessModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_domain.usecases.AuthCheckIMEIUseCase
import com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_models.get_auth_configurations.EkycStepAuthType
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import org.koin.compose.koinInject

@Composable
fun CheckIMEIAuthScreenContent(
    authViewModel: AuthViewModel,
    navController: NavController,
) {
    val context = LocalContext.current
    val authCheckIMEIUseCase =
        AuthCheckIMEIUseCase(koinInject())

    val checkIMEIAuthViewModel =
        remember {
            CheckIMEIAuthViewModel(authCheckIMEIUseCase = authCheckIMEIUseCase, context = context)
        }
    val checkIMEIAuthVM = remember { checkIMEIAuthViewModel }


    val activity = context.findActivity()
    val loading = checkIMEIAuthViewModel.loading.collectAsState()
    val failure = checkIMEIAuthViewModel.failure.collectAsState()
    val imeiChecked = checkIMEIAuthVM.imeiChecked.collectAsState()


    BackGroundView(navController = navController, showAppBar = false) {

        if (imeiChecked.value) {
            val isEmpty =
                authViewModel.removeCurrentStep(EkycStepAuthType.IME.getStepId())
            if (isEmpty)
                DialogView(
                    bottomSheetStatus = BottomSheetStatus.SUCCESS,
                    text = stringResource(id = R.string.successfulAuthentication),
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
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                        },
                    )
                }
            }
        }

    }
}
