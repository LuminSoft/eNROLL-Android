package com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_onboarding.ui.components

import UpdateScanType
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import appColors
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_navigation.updateNationalIdBackConfirmationScreen
import com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_navigation.updateNationalIdErrorScreen
import com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_navigation.updateNationalIdFrontConfirmationScreen
import com.luminsoft.enroll_sdk.innovitices.activities.DocumentActivity
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import findActivity


@Composable
fun UpdateNationalIdErrorScreen(
    navController: NavController,
    updateViewModel: UpdateViewModel
) {
    
    
    val rememberedViewModel = remember { updateViewModel }
    val context = LocalContext.current
    val activity = context.findActivity()
    val errorMessage = updateViewModel.errorMessage.collectAsState()
    val scanType = updateViewModel.scanType.collectAsState()
    val loading = updateViewModel.loading.collectAsState()

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val documentFrontUri = it.data?.data
            if (documentFrontUri != null) {
                try {
                    val facialDocumentModel =
                        DotHelper.documentNonFacial(documentFrontUri, activity)
                    rememberedViewModel.nationalIdFrontImage.value =
                        facialDocumentModel.documentImageBase64
                    navController.navigate(updateNationalIdFrontConfirmationScreen)
                } catch (e: Exception) {
                    updateViewModel.disableLoading()
                    updateViewModel.errorMessage.value = e.message
                    updateViewModel.scanType.value = UpdateScanType.FRONT
                    navController.navigate(updateNationalIdErrorScreen)
                    println(e.message)
                }
            } else if (it.resultCode == 19 || it.resultCode == 8) {
                updateViewModel.disableLoading()
                updateViewModel.errorMessage.value =
                    context.getString(R.string.timeoutException)
                updateViewModel.scanType.value = UpdateScanType.FRONT
                navController.navigate(updateNationalIdErrorScreen)
            }
        }

    val startForBackResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val documentBackUri = it.data?.data
            if (documentBackUri != null) {
                try {
                    val nonFacialDocumentModel =
                        DotHelper.documentNonFacial(documentBackUri, activity)
                    updateViewModel.nationalIdBackImage.value =
                        nonFacialDocumentModel.documentImageBase64
                    navController.navigate(updateNationalIdBackConfirmationScreen)
                } catch (e: Exception) {
                    updateViewModel.disableLoading()
                    updateViewModel.errorMessage.value = e.message
                    updateViewModel.scanType.value = UpdateScanType.Back
                    navController.navigate(updateNationalIdErrorScreen)
                    println(e.message)
                }
            } else if (it.resultCode == 19 || it.resultCode == 8) {
                updateViewModel.disableLoading()
                updateViewModel.errorMessage.value =
                    context.getString(R.string.timeoutException)
                updateViewModel.scanType.value = UpdateScanType.Back
                navController.navigate(updateNationalIdErrorScreen)
            }
        }


    Surface(modifier = Modifier.fillMaxSize()) {
        Image(
            painterResource(R.drawable.blured_bg),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        if (loading.value) LoadingView()
        else
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)

            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.25f))
                Image(
                    painterResource(R.drawable.invalid_ni_icon),
                    contentDescription = "",
                    colorFilter =   ColorFilter.tint(MaterialTheme.appColors.primary),
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight(0.35f)
                )
                Spacer(modifier = Modifier.height(30.dp))
                errorMessage.value?.let { Text(text = it) }
                Spacer(modifier = Modifier.fillMaxHeight(0.35f))

                ButtonView(
                    onClick = {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(
                            EnrollFailedModel(
                                errorMessage.value!!, errorMessage
                            )
                        )
                    }, title = stringResource(id = R.string.exit)
                )
                Spacer(modifier = Modifier.height(20.dp))

                ButtonView(
                    onClick = {
                        rememberedViewModel.enableLoading()
                        val intent =
                            Intent(activity.applicationContext, DocumentActivity::class.java)
                        when (scanType.value) {
                            UpdateScanType.FRONT -> {
                                intent.putExtra("scanType", DocumentActivity().frontScan)
                                intent.putExtra("localCode", EnrollSDK.localizationCode.name)

                                startForResult.launch(intent)
                            }

                            UpdateScanType.Back -> {
                                intent.putExtra("scanType", DocumentActivity().backScan)
                                intent.putExtra("localCode", EnrollSDK.localizationCode.name)

                                startForBackResult.launch(intent)
                            }

                            null -> {
                                intent.putExtra("scanType", DocumentActivity().frontScan)
                                intent.putExtra("localCode", EnrollSDK.localizationCode.name)

                                startForResult.launch(intent)
                            }
                        }
                    },
                    title = stringResource(id = R.string.reScan),
                    color = MaterialTheme.appColors.backGround,
                    borderColor = MaterialTheme.appColors.primary,
                    textColor = MaterialTheme.appColors.primary
                )
            }
    }
}


