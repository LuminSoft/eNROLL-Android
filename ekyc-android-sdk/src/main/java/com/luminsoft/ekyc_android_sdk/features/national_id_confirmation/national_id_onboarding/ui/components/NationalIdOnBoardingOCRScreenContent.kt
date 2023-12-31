package com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.view_model.NationalIdFrontOcrViewModel
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import org.koin.compose.koinInject


@Composable
fun NationalIdOnBoardingFrontConfirmationScreen(
    navController:NavController,
    onBoardingViewModel: OnBoardingViewModel
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val document = onBoardingViewModel.nationalIdFrontImage.collectAsState()
    var nationalIdFrontOcrVM =
        document.value?.let { NationalIdFrontOcrViewModel(koinInject(),koinInject(), it) }

    var nationalIdFrontOcrViewModel = remember { nationalIdFrontOcrVM }


}
