package com.luminsoft.ekyc_android_sdk.features.location.national_id_onboarding.ui.components


import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.features.location.national_id_onboarding.view_model.LocationOnBoardingViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun LocationOnBoardingScreenContent(
    locationOnBoardingViewModel: LocationOnBoardingViewModel = koinViewModel<LocationOnBoardingViewModel>(),
    navController: NavController,
) {

}
