package com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.ui.components

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.core.sdk.EkycSdk
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingErrorScreen
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingFrontConfirmationScreen
import com.luminsoft.ekyc_android_sdk.innovitices.activities.DocumentActivity
import com.luminsoft.ekyc_android_sdk.innovitices.core.DotHelper
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.get_onboaring_configurations.RegistrationStepSetting
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.ui_components.components.BackGroundView
import com.luminsoft.ekyc_android_sdk.ui_components.components.ButtonView
import com.luminsoft.ekyc_android_sdk.ui_components.components.EkycItemView
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun NationalIdOnBoardingPreScanScreen(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {
    val rememberedViewModel = remember { onBoardingViewModel }
    val context = LocalContext.current
    val activity = context.findActivity()

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val documentFrontUri = it.data?.data
            if (documentFrontUri != null) {
                try {
                    val facialDocumentModel =
                        DotHelper.documentDetectFace(documentFrontUri, activity)
                    rememberedViewModel.faceImage.value = facialDocumentModel.faceImage
                    rememberedViewModel.nationalIdFrontImage.value =
                        facialDocumentModel.documentImage
                    navController.navigate(nationalIdOnBoardingFrontConfirmationScreen)
                } catch (e: Exception) {
                    onBoardingViewModel.errorMessage.value = e.message
                    onBoardingViewModel.scanType.value = ScanType.FRONT
                    navController.navigate(nationalIdOnBoardingErrorScreen)
                    println(e.message)
                }
            }
        }

    val startPassportForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val documentFrontUri = it.data?.data
            if (documentFrontUri != null) {
                try {
                    val facialDocumentModel =
                        DotHelper.documentDetectFace(documentFrontUri, activity)
                    rememberedViewModel.faceImage.value = facialDocumentModel.faceImage
                    rememberedViewModel.nationalIdFrontImage.value =
                        facialDocumentModel.documentImage
                    navController.navigate(nationalIdOnBoardingFrontConfirmationScreen)
                } catch (e: Exception) {
                    //TODO handle error
                    println(e.message)
                }
            }
        }

    BackGroundView(navController = navController, showAppBar = false) {

        onBoardingViewModel.currentStepId = MutableStateFlow(1)

        for (i in organizationRegStepSettings(rememberedViewModel)) {
            when (i.parseRegistrationStepSetting()) {
                RegistrationStepSetting.nationalIdOnly -> {
                    NationalIdOnly(activity, startForResult)
                }

                RegistrationStepSetting.passportOnly -> {
                    PassportOnly(activity, startPassportForResult)
                }

                RegistrationStepSetting.nationalIdOrPassport -> {
                    NationalIdOnly(activity, startForResult)

                }

                RegistrationStepSetting.nationalIdAndPassport -> {
                    NationalIdOnly(activity, startForResult)

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
    startForResult: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        EkycItemView(R.drawable.step_01_national_id, R.string.documentPreScanContent)
        ButtonView(
            onClick = {
                val intent = Intent(activity.applicationContext, DocumentActivity::class.java)
                intent.putExtra("scanType", DocumentActivity().FRONT_SCAN)
                intent.putExtra("localCode", EkycSdk.localizationCode.name)

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
    startForResult: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        EkycItemView(R.drawable.step_01_passport, R.string.passportPreScanContent)
        ButtonView(
            onClick = {
                val intent = Intent(activity.applicationContext, DocumentActivity::class.java)
                intent.putExtra("scanType", DocumentActivity().PASSPORT_SCAN)
                intent.putExtra("localCode", EkycSdk.localizationCode.name)

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
