package com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_onboarding.ui.components.FaceCaptureBoardingPostScanScreenContent
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_onboarding.ui.components.FaceCaptureBoardingPreScanScreenContent
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_onboarding.ui.components.FaceCaptureOnBoardingErrorScreen
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel

const val faceCaptureBoardingPreScanScreenContent = "faceCaptureBoardingPreScanScreenContent"
const val faceCaptureBoardingPostScanScreenContent =
    "faceCaptureBoardingPostScanScreenContent"
const val faceCaptureOnBoardingErrorScreen =
    "faceCaptureOnBoardingErrorScreen"

fun NavGraphBuilder.faceCaptureRouter(
    navController: NavController, onBoardingViewModel: OnBoardingViewModel
) {
    composable(route = faceCaptureBoardingPreScanScreenContent) {
        FaceCaptureBoardingPreScanScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel
        )
    }
    composable(route = faceCaptureBoardingPostScanScreenContent) {
        FaceCaptureBoardingPostScanScreenContent(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel
        )
    }
    composable(route = faceCaptureOnBoardingErrorScreen) {
        FaceCaptureOnBoardingErrorScreen(
            navController = navController,
            onBoardingViewModel = onBoardingViewModel
        )
    }
}
