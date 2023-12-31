package com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingFrontConfirmationScreen
import com.luminsoft.ekyc_android_sdk.innovitices.activities.DocumentActivity
import com.luminsoft.ekyc_android_sdk.innovitices.core.DotHelper
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.ui_components.components.BackGroundView
import com.luminsoft.ekyc_android_sdk.ui_components.components.ButtonView
import com.luminsoft.ekyc_android_sdk.ui_components.components.EkycItemView


@Composable
fun NationalIdOnBoardingPrescanScreen(
    navController: NavController,
    isSavedCards: Boolean = true,
    onBoardingViewModel: OnBoardingViewModel
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    var code = remember { mutableStateOf(0) }
    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            val documentFrontUri = it.data?.data
            if (documentFrontUri != null) {
                val facialDocumentModel = DotHelper.documentDetectFace(documentFrontUri, activity)
                onBoardingViewModel.faceImage.value = facialDocumentModel.faceImage
                onBoardingViewModel.nationalIdFrontImage.value = facialDocumentModel.documentImage
                navController.navigate(nationalIdOnBoardingFrontConfirmationScreen)
            }
        }

    BackGroundView(navController = navController, showAppBar = false) {
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
                    startForResult.launch(intent)
                },
                stringResource(id = R.string.start),
                modifier = Modifier.padding(horizontal = 20.dp),
            )
            Spacer(modifier = Modifier.safeContentPadding().height(10.dp))
        }
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
