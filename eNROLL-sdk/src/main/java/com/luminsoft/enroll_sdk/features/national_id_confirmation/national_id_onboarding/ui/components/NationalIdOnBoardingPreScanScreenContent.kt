package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_navigation.faceCaptureOnBoardingErrorScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingErrorScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingFrontConfirmationScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.passportOnBoardingConfirmationScreen
import com.luminsoft.enroll_sdk.innovitices.activities.DocumentActivity
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.RegistrationStepSetting
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.EnrollItemView
import com.luminsoft.enroll_sdk.ui_components.components.SpinKitLoadingIndicator


@Composable
fun NationalIdOnBoardingPreScanScreen(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {
    val rememberedViewModel = remember { onBoardingViewModel }
    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = onBoardingViewModel.loading.collectAsState()

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val documentFrontUri = it.data?.data
            if (documentFrontUri != null) {
                try {
                    val facialDocumentModel =
                        DotHelper.documentNonFacial(documentFrontUri, activity)
                    rememberedViewModel.nationalIdFrontImage.value =
                        facialDocumentModel.documentImageBase64
                    navController.navigate(nationalIdOnBoardingFrontConfirmationScreen)
                } catch (e: Exception) {
                    onBoardingViewModel.disableLoading()
                    onBoardingViewModel.errorMessage.value = e.message
                    onBoardingViewModel.scanType.value = ScanType.FRONT
                    navController.navigate(nationalIdOnBoardingErrorScreen)
                    println(e.message)
                }
            } else if (it.resultCode == 19 || it.resultCode == 8) {
                onBoardingViewModel.disableLoading()
                onBoardingViewModel.errorMessage.value =
                    context.getString(R.string.timeoutException)
                onBoardingViewModel.scanType.value = ScanType.FRONT
                navController.navigate(nationalIdOnBoardingErrorScreen)
            }
        }

    val startPassportForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val documentFrontUri = it.data?.data
            if (documentFrontUri != null) {
                try {
                    val facialDocumentModel =
                        DotHelper.documentNonFacial(documentFrontUri, activity)
//                    rememberedViewModel.faceImage.value = facialDocumentModel.faceImage
                    rememberedViewModel.passportImage.value =
                        facialDocumentModel.documentImageBase64
                    navController.navigate(passportOnBoardingConfirmationScreen)
                } catch (e: Exception) {
                    onBoardingViewModel.disableLoading()
                    onBoardingViewModel.errorMessage.value = e.message
                    onBoardingViewModel.scanType.value = ScanType.PASSPORT
                    navController.navigate(nationalIdOnBoardingErrorScreen)
                    println(e.message)
                }
            } else if (it.resultCode == 19 || it.resultCode == 8) {
                onBoardingViewModel.disableLoading()
                onBoardingViewModel.errorMessage.value =
                    context.getString(R.string.timeoutException)
                onBoardingViewModel.scanType.value = ScanType.PASSPORT
                navController.navigate(nationalIdOnBoardingErrorScreen)
            }
        }

    BackGroundView(navController = navController, showAppBar = false) {
        if (loading.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { SpinKitLoadingIndicator() }
        } else
            for (i in organizationRegStepSettings(rememberedViewModel)) {
                when (i.parseRegistrationStepSetting()) {
                    RegistrationStepSetting.nationalIdOnly -> {
                        NationalIdOnly(activity, startForResult, rememberedViewModel)
                    }

                    RegistrationStepSetting.passportOnly -> {
                        PassportOnly(activity, startPassportForResult, rememberedViewModel)
                    }

                    RegistrationStepSetting.nationalIdOrPassport -> {
                        NationalIdOnly(activity, startForResult, rememberedViewModel)

                    }

                    RegistrationStepSetting.nationalIdAndPassport -> {
                        NationalIdOnly(activity, startForResult, rememberedViewModel)

                    }

                    else -> {
                        continue
                    }
                }
            }

    }

}

private fun organizationRegStepSettings(rememberedViewModel: OnBoardingViewModel) =
    rememberedViewModel.steps.value?.first()?.organizationRegStepSettings!!

@Composable
private fun NationalIdOnly(
    activity: Activity,
    startForResult: ManagedActivityResultLauncher<Intent, ActivityResult>,
    rememberedViewModel: OnBoardingViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        EnrollItemView(R.drawable.step_01_national_id, R.string.documentPreScanContent)
        ButtonView(
            onClick = {
                rememberedViewModel.enableLoading()
                val intent = Intent(activity.applicationContext, DocumentActivity::class.java)
                intent.putExtra("scanType", DocumentActivity().FRONT_SCAN)
                intent.putExtra("localCode", EnrollSDK.localizationCode.name)

                startForResult.launch(intent)
            },
            stringResource(id = R.string.start),
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        Spacer(
            modifier = Modifier
                .safeContentPadding()
                .height(10.dp)
        )
    }
}

@Composable
private fun PassportOnly(
    activity: Activity,
    startForResult: ManagedActivityResultLauncher<Intent, ActivityResult>,
    rememberedViewModel: OnBoardingViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        EnrollItemView(R.drawable.step_01_passport, R.string.passportPreScanContent)
        ButtonView(
            onClick = {
                rememberedViewModel.enableLoading()

                val intent = Intent(activity.applicationContext, DocumentActivity::class.java)
                intent.putExtra("scanType", DocumentActivity().PASSPORT_SCAN)
                intent.putExtra("localCode", EnrollSDK.localizationCode.name)

                startForResult.launch(intent)
            },
            stringResource(id = R.string.start),
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        Spacer(
            modifier = Modifier
                .safeContentPadding()
                .height(10.dp)
        )
    }
}

internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}
