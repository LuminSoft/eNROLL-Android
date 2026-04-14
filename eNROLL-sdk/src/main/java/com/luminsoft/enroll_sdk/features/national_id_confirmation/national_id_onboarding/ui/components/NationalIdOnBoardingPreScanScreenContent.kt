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
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.luminsoft.enroll_sdk.ui_components.theme.IconRenderingMode
import com.luminsoft.enroll_sdk.ui_components.theme.resolveUiIcon
import com.luminsoft.enroll_sdk.ui_components.theme.resolvedPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.models.EnrollForcedDocumentType
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.widgets.ImagesBox
import com.luminsoft.enroll_sdk.ui_components.theme.ResolvedStepIcon
import com.luminsoft.enroll_sdk.ui_components.theme.appIcons
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingErrorScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingFrontConfirmationScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.passportOnBoardingConfirmationScreen
import com.luminsoft.enroll_sdk.core.utils.NfcUtils
import com.luminsoft.enroll_sdk.innovitices.activities.DocumentActivity
import com.luminsoft.enroll_sdk.innovitices.activities.EPassportActivity
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.ChooseStep
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.RegistrationStepSetting
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.EnrollItemView
import com.luminsoft.enroll_sdk.ui_components.components.SpinKitLoadingIndicator
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.theme.AppColors
import com.luminsoft.enroll_sdk.ui_components.theme.ConstantColors
import com.luminsoft.enroll_sdk.ui_components.theme.appColors
import android.provider.Settings
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


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

    val startEPassportForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onBoardingViewModel.disableLoading()
            if (it.resultCode == Activity.RESULT_OK) {
                onBoardingViewModel.removeCurrentStep(
                    com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.EkycStepType.PersonalConfirmation.getStepId()
                )
            } else if (it.resultCode == Activity.RESULT_CANCELED) {
                val nfcError = it.data?.getStringExtra(EPassportActivity.OUT_NFC_ERROR)
                if (nfcError != null) {
                    onBoardingViewModel.errorMessage.value = nfcError
                    onBoardingViewModel.scanType.value = ScanType.PASSPORT
                    navController.navigate(nationalIdOnBoardingErrorScreen)
                }
            }
        }

    val hasNfc = remember { NfcUtils.hasNfcSupport(context) }

    BackGroundView(navController = navController, showAppBar = false) {
        if (loading.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { SpinKitLoadingIndicator() }
        } else if (isPassportAndMailFinal.value) {
            if (hasNfc) {
                PassportOrEPassport(chosenStep, rememberedViewModel, activity, startPassportForResult, startEPassportForResult)
            } else {
                PassportOnly(activity, startPassportForResult, rememberedViewModel)
            }
        } else if (selectedStep.value != null) {
            if (selectedStep.value == ChooseStep.NationalId)
                NationalIdOnly(activity, startForResult, rememberedViewModel)
            else if (selectedStep.value == ChooseStep.Passport)
                PassportOnly(activity, startPassportForResult, rememberedViewModel)
            else if (selectedStep.value == ChooseStep.EPassport)
                EPassportOnly(activity, startEPassportForResult, rememberedViewModel)
        } else
            for (i in organizationRegStepSettings(rememberedViewModel)) {
                when (i.parseRegistrationStepSetting()) {
                    RegistrationStepSetting.nationalIdOnly -> {
                        NationalIdOnly(activity, startForResult, rememberedViewModel)
                    }

                    RegistrationStepSetting.passportOnly -> {
                        if (hasNfc) {
                            // Device supports NFC - show ePassport option
                            PassportOrEPassport(chosenStep, rememberedViewModel, activity, startPassportForResult, startEPassportForResult)
                        } else {
                            // Device doesn't support NFC - show only National ID
                            NationalIdOnly(activity, startForResult, rememberedViewModel)
                        }
                    }

                    (RegistrationStepSetting.nationalIdOrPassport) -> {
                        when (EnrollSDK.enrollForcedDocumentType) {
                            EnrollForcedDocumentType.NATIONAL_ID_ONLY -> {
                                NationalIdOnly(activity, startForResult, rememberedViewModel)
                            }

                            EnrollForcedDocumentType.PASSPORT_ONLY -> {
                                if (hasNfc) {
                                    // Device supports NFC - show ePassport option
                                    PassportOrEPassport(chosenStep, rememberedViewModel, activity, startPassportForResult, startEPassportForResult)
                                } else {
                                    // Device doesn't support NFC - show only National ID
                                    NationalIdOnly(activity, startForResult, rememberedViewModel)
                                }
                            }

                            else -> {
                                NationalIdOrPassport(chosenStep, rememberedViewModel)
                            }
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
    rememberedViewModel.steps.value?.first()?.organizationRegStepSettings ?: emptyList()

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
            ), R.string.documentPreScanContent, MaterialTheme.appIcons.nationalId.preScan
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
    val context = LocalContext.current
    val hasNfc = remember { NfcUtils.hasNfcSupport(context) }
    val scrollState = rememberScrollState()
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = stringResource(id = R.string.choosePersonalConfirmation),
            fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
        )
        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider(
            modifier = Modifier.width(50.dp),
            thickness = 3.dp,
            color = MaterialTheme.appColors.secondary
        )

        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(ChooseStep.NationalId, chosenStep, rememberedViewModel)
            // Card(ChooseStep.Passport, chosenStep, rememberedViewModel) // Commented out - only ePassport is supported
            if (hasNfc) {
                Card(ChooseStep.EPassport, chosenStep, rememberedViewModel)
            }

        }
        Spacer(modifier = Modifier.height(30.dp))

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
            
            val epassportImages = listOf(
                R.drawable.passport_scan_01,
                R.drawable.passport_scan_02,
                R.drawable.passport_scan_03
            )
            
            val images = when (step) {
                ChooseStep.NationalId -> idImages
                ChooseStep.Passport -> passportImages
                ChooseStep.EPassport -> epassportImages
            }

            val customIcon = when (step) {
                ChooseStep.NationalId -> MaterialTheme.appIcons.nationalId.choose
                ChooseStep.Passport -> MaterialTheme.appIcons.passport.choose
                ChooseStep.EPassport -> MaterialTheme.appIcons.passport.choose
            }
            
            Box(modifier = Modifier.fillMaxHeight(0.35f), contentAlignment = Alignment.Center) {
                Image(
                    painterResource(R.drawable.icon_back_shape),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxHeight(1f)
                )
                ResolvedStepIcon(
                    customIcon = customIcon,
                    modifier = Modifier.fillMaxHeight(0.7f),
                    defaultContent = {
                        ImagesBox(
                            images = images,
                            modifier = Modifier.fillMaxHeight(0.7f)
                        )
                    }
                )

            }



            Spacer(modifier = Modifier.height(20.dp))
            
            val titleRes = when (step) {
                ChooseStep.NationalId -> R.string.nationalId
                ChooseStep.Passport -> R.string.passport
                ChooseStep.EPassport -> R.string.epassport
            }
            
            val descriptionRes = when (step) {
                ChooseStep.NationalId -> R.string.nationalIdDescription
                ChooseStep.Passport -> R.string.passportDescription
                ChooseStep.EPassport -> R.string.epassportDescription
            }
            
            Text(
                text = stringResource(id = titleRes),
                fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                val customInfoIcon = resolveUiIcon(R.drawable.info_icon)
                Image(
                    resolvedPainter(customInfoIcon, R.drawable.info_icon),
                    contentDescription = "",
                    colorFilter = if (customInfoIcon?.renderingMode == IconRenderingMode.ORIGINAL) null
                        else ColorFilter.tint(MaterialTheme.appColors.secondary),
                    modifier = Modifier
                        .size(15.dp)
                )
                Spacer(Modifier.width(5.dp))
                Box(Modifier.fillMaxWidth(0.9f)) {
                    Text(
                        text = stringResource(id = descriptionRes),
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
            ), R.string.passportPreScanContent, MaterialTheme.appIcons.passport.preScan
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

@Composable
private fun PassportOrEPassport(
    chosenStep: State<ChooseStep?>,
    rememberedViewModel: OnBoardingViewModel,
    activity: Activity,
    startPassportForResult: ManagedActivityResultLauncher<Intent, ActivityResult>,
    startEPassportForResult: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val selectedStep = rememberedViewModel.selectedStep.collectAsState()

    // Commented out normal passport - only ePassport is supported
    // if (selectedStep.value == ChooseStep.Passport) {
    //     PassportOnly(activity, startPassportForResult, rememberedViewModel)
    // } else 
    if (selectedStep.value == ChooseStep.EPassport) {
        EPassportOnly(activity, startEPassportForResult, rememberedViewModel)
    } else {
        // Directly show ePassport option without selection screen
        // since normal passport is no longer supported
        EPassportOnly(activity, startEPassportForResult, rememberedViewModel)
        
        /* Original selection UI - commented out since only ePassport is supported
        val scrollState = rememberScrollState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))

            Text(
                text = stringResource(id = R.string.choosePersonalConfirmation),
                fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
            )
            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                modifier = Modifier.width(50.dp),
                thickness = 3.dp,
                color = MaterialTheme.appColors.secondary
            )

            Spacer(modifier = Modifier.height(40.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(ChooseStep.Passport, chosenStep, rememberedViewModel)
                Card(ChooseStep.EPassport, chosenStep, rememberedViewModel)
            }
            Spacer(modifier = Modifier.height(30.dp))

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
        */
    }
}

@Composable
private fun EPassportOnly(
    activity: Activity,
    startEPassportForResult: ManagedActivityResultLauncher<Intent, ActivityResult>,
    rememberedViewModel: OnBoardingViewModel
) {
    val context = LocalContext.current
    var showNfcDisabledDialog by remember { mutableStateOf(false) }

    if (showNfcDisabledDialog) {
        DialogView(
            bottomSheetStatus = BottomSheetStatus.WARNING,
            text = stringResource(id = R.string.nfc_disabled_message),
            buttonText = stringResource(id = R.string.nfc_open_settings),
            onPressedButton = {
                showNfcDisabledDialog = false
                context.startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            },
            secondButtonText = stringResource(id = R.string.cancel),
            onPressedSecondButton = {
                showNfcDisabledDialog = false
            },
            onDismiss = {
                showNfcDisabledDialog = false
            }
        )
    }

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
            ), R.string.epassportPreScanContent, MaterialTheme.appIcons.passport.ePassportPreScan
        )
        ButtonView(
            onClick = {
                if (!NfcUtils.isNfcEnabled(context)) {
                    showNfcDisabledDialog = true
                    return@ButtonView
                }
                rememberedViewModel.enableLoading()

                val intent = Intent(activity.applicationContext, EPassportActivity::class.java)
                intent.putExtra("localCode", EnrollSDK.localizationCode.name)

                startEPassportForResult.launch(intent)
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
