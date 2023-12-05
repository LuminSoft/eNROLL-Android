package com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_onboarding.ui.components.FaceCaptureBoardingPrescanScreenContent

const val faceCaptureBoardingPrescanScreenContent = "faceCaptureBoardingPrescanScreenContent"

fun NavController.navigateFaceCapture(navOptions: NavOptions? = null) {
    this.navigate(faceCaptureBoardingPrescanScreenContent, navOptions)
}

fun NavGraphBuilder.faceCaptureRouter(navController: NavController) {
    composable(route = faceCaptureBoardingPrescanScreenContent) {
        FaceCaptureBoardingPrescanScreenContent(navController=navController)
    }
}