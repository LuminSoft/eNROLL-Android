package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.GetCountriesUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.PhoneInfoUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.view_model.PhoneNumbersOnBoardingViewModel
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import org.koin.compose.koinInject

@Composable
fun PhoneNumbersOnBoardingScreenContent(
    onBoardingViewModel: OnBoardingViewModel,
    navController: NavController,
) {
    val getCountriesUseCase =
        GetCountriesUseCase(koinInject())
    val phoneInfoUseCase =
        PhoneInfoUseCase(koinInject())

    val phoneNumbersOnBoardingViewModel =
        remember {
            PhoneNumbersOnBoardingViewModel(
                getCountriesUseCase = getCountriesUseCase,
                phoneInfoUseCase = phoneInfoUseCase
            )
        }
    val phoneNumbersOnBoardingVM = remember { phoneNumbersOnBoardingViewModel }

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = phoneNumbersOnBoardingViewModel.loading.collectAsState()
    val failure = phoneNumbersOnBoardingViewModel.failure.collectAsState()
    val countries = phoneNumbersOnBoardingViewModel.countries.collectAsState()

    BackGroundView(navController = navController, showAppBar = false) {
//        if (locationSent.value) {
//            onBoardingViewModel.removeCurrentStep(6)
//        }


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
                            phoneNumbersOnBoardingVM.callGetCountries()
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
        } else if (countries.value != null)
            countries.value!!.first().name?.let { Text(text = it) }
    }


}
