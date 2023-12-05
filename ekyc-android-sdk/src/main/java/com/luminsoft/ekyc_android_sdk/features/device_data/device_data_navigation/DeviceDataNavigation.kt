package com.luminsoft.ekyc_android_sdk.features.device_data.device_data_navigation

import DeviceDataOnBoardingPrescanScreenContent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val deviceDataOnBoardingPrescanScreenContent = "deviceDataOnBoardingPrescanScreenContent"

fun NavController.navigateToDeviceData(navOptions: NavOptions? = null) {
    this.navigate(deviceDataOnBoardingPrescanScreenContent, navOptions)
}

fun NavGraphBuilder.deviceDataRouter(navController: NavController) {

    composable(route = deviceDataOnBoardingPrescanScreenContent) {
        DeviceDataOnBoardingPrescanScreenContent(navController=navController)
    }
}