package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.widgets.ImagesBox
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingErrorScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingFrontConfirmationScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.passportOnBoardingConfirmationScreen
import com.luminsoft.enroll_sdk.innovitices.activities.DocumentActivity
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.ChooseStep
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.RegistrationStepSetting
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.EnrollItemView
import com.luminsoft.enroll_sdk.ui_components.components.SpinKitLoadingIndicator
import com.luminsoft.enroll_sdk.ui_components.theme.AppColors
import com.luminsoft.enroll_sdk.ui_components.theme.ConstantColors
import com.luminsoft.enroll_sdk.ui_components.theme.appColors


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NationalIdOnBoardingPreScanScreen(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {
    val rememberedViewModel = remember { onBoardingViewModel }
    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = onBoardingViewModel.loading.collectAsState()
    val chosenStep = onBoardingViewModel.chosenStep.collectAsState()
    val selectedStep = onBoardingViewModel.selectedStep.collectAsState()
    val isPassportAndMailFinal = remember { rememberedViewModel.isPassportAndMailFinal }

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
        } else if (isPassportAndMailFinal.value) {
            PassportOnly(activity, startPassportForResult, rememberedViewModel)
        } else if (selectedStep.value != null) {
            if (selectedStep.value == ChooseStep.NationalId)
                NationalIdOnly(activity, startForResult, rememberedViewModel)
            else if (selectedStep.value == ChooseStep.Passport)
                PassportOnly(activity, startPassportForResult, rememberedViewModel)
        } else
            for (i in organizationRegStepSettings(rememberedViewModel)) {
                when (i.parseRegistrationStepSetting()) {
                    RegistrationStepSetting.nationalIdOnly -> {
                        NationalIdOnly(activity, startForResult, rememberedViewModel)
                    }

                    RegistrationStepSetting.passportOnly -> {
                        PassportOnly(activity, startPassportForResult, rememberedViewModel)
                    }

                    (RegistrationStepSetting.nationalIdOrPassport) -> {
                        if (EnrollSDK.egyptianNationalId) {
                            NationalIdOnly(activity, startForResult, rememberedViewModel)
                        } else {
                            NationalIdOrPassport(chosenStep, rememberedViewModel)
                        }

                    }

                    RegistrationStepSetting.nationalIdAndPassport -> {

                        onBoardingViewModel.isPassportAndMail.value = true
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
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        EnrollItemView(
            listOf(
                R.drawable.step_01_national_id_1,
                R.drawable.step_01_national_id_2,
                R.drawable.step_01_national_id_3
            ), R.string.documentPreScanContent
        )
        ButtonView(
            onClick = {
                rememberedViewModel.enableLoading()
                val intent = Intent(activity.applicationContext, DocumentActivity::class.java)
                intent.putExtra("scanType", DocumentActivity().frontScan)
                intent.putExtra("localCode", EnrollSDK.localizationCode.name)

                startForResult.launch(intent)
            },
            stringResource(id = R.string.start),
        )
        Spacer(
            modifier = Modifier
                .safeContentPadding()
                .height(10.dp)
        )
    }
}

@Composable
private fun NationalIdOrPassport(
    chosenStep: State<ChooseStep?>,
    rememberedViewModel: OnBoardingViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.10f))

        Text(text = stringResource(id = R.string.choosePersonalConfirmation),fontFamily = MaterialTheme.typography.labelLarge.fontFamily,)
        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider(
            modifier = Modifier.width(50.dp),
            thickness = 3.dp,
            color = MaterialTheme.appColors.secondary
        )

        Spacer(modifier = Modifier.height(80.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ) {

            Card(ChooseStep.NationalId, chosenStep, rememberedViewModel)
            Card(ChooseStep.Passport, chosenStep, rememberedViewModel)

        }
        Spacer(modifier = Modifier.fillMaxHeight(0.2f))

        ButtonView(
            onClick = {
                rememberedViewModel.selectedStep.value = chosenStep.value
            },
            stringResource(id = R.string.continue_to_next),
        )
        Spacer(
            modifier = Modifier
                .safeContentPadding()
                .height(10.dp)
        )
    }
}

@Composable
private fun Card(
    step: ChooseStep,
    chosenStep: State<ChooseStep?>,
    rememberedViewModel: OnBoardingViewModel
) {
    val alpha = if (step == chosenStep.value!!) 1.0f else 0.3f
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (step == chosenStep.value!!) Color.White else ConstantColors.onBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = Modifier
            .border(
                width = if (step != chosenStep.value!!) 1.dp else 0.dp,
                color = ConstantColors.onSecondaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .alpha(alpha)
            .clickable(step != chosenStep.value!!) {
                rememberedViewModel.chosenStep.value = step
            },

        ) {
        Column(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            val idImages = listOf(
                R.drawable.id_scan_01, R.drawable.id_scan_02, R.drawable.id_scan_03
            )

            val passportImages = listOf(
                R.drawable.passport_scan_01,
                R.drawable.passport_scan_02,
                R.drawable.passport_scan_03
            )
            Box(modifier = Modifier.fillMaxHeight(0.35f), contentAlignment = Alignment.Center) {
                Image(
                    painterResource(R.drawable.icon_back_shape),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxHeight(1f)
                )
                ImagesBox(
                    images = if (step == ChooseStep.NationalId) idImages else passportImages,
                    modifier = Modifier.fillMaxHeight(0.7f)
                )

            }



            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = if (step == ChooseStep.NationalId) R.string.nationalId else R.string.passport),
                fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                Image(
                    painterResource(R.drawable.info_icon),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.appColors.secondary),

                    modifier = Modifier
                        .size(15.dp)

                )
                Spacer(Modifier.width(5.dp))
                Box(Modifier.fillMaxWidth(0.9f)) {
                    Text(
                        text = stringResource(id = if (step == ChooseStep.NationalId) R.string.nationalIdDescription else R.string.passportDescription),
                        fontSize = 9.sp,
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                        color = AppColors().appBlack,
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp,
                        modifier = Modifier.align(Alignment.TopStart)
                    )
                }
            }
        }

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
        EnrollItemView(
            listOf(
                R.drawable.step_01_passport_1,
                R.drawable.step_01_passport_2,
                R.drawable.step_01_passport_3
            ), R.string.passportPreScanContent
        )
        ButtonView(
            onClick = {
                rememberedViewModel.enableLoading()

                val intent = Intent(activity.applicationContext, DocumentActivity::class.java)
                intent.putExtra("scanType", DocumentActivity().passportScan)
                intent.putExtra("localCode", EnrollSDK.localizationCode.name)

                startForResult.launch(intent)
            },
            stringResource(id = R.string.start),
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
