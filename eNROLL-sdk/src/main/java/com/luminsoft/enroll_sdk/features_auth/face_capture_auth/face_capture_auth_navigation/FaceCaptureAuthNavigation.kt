package com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth.ui.components.FaceCaptureAuthErrorScreen
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth.ui.components.FaceCaptureAuthPostScanScreenContent
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth.ui.components.FaceCaptureAuthPreScanScreenContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_presentation.main_auth.view_model.AuthViewModel

const val faceCaptureAuthPreScanScreenContent = "faceCaptureAuthPreScanScreenContent"
const val faceCaptureAuthPostScanScreenContent = "faceCaptureAuthPostScanScreenContent"
const val faceCaptureAuthErrorScreen = "faceCaptureAuthErrorScreen"

fun NavGraphBuilder.faceCaptureAuthRouter(
    navController: NavController, authViewModel: AuthViewModel
) {
    composable(route = faceCaptureAuthPreScanScreenContent) {
        FaceCaptureAuthPreScanScreenContent(
            navController = navController,
            authViewModel = authViewModel
        )
    }
    composable(route = faceCaptureAuthPostScanScreenContent) {
        FaceCaptureAuthPostScanScreenContent(
            navController = navController,
            authViewModel = authViewModel
        )
    }

    composable(route = faceCaptureAuthErrorScreen) {
        FaceCaptureAuthErrorScreen(
            navController = navController,
            authViewModel = authViewModel
        )
    }

}
