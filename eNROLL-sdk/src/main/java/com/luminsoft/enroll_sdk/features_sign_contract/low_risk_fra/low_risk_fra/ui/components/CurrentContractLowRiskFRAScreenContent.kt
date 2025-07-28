package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra.ui.components

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.EnrollSuccessModel
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.lightenColor
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra.view_model.CurrentContractLowRiskFRAViewModel
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases.GetCurrentContractLowRiskFRAUseCase
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases.GetSignContractFileLowRiskFRAUseCase
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_navigation.signContractScreenContent
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_sign_contract_steps.ContractFileModel
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_presentation.main_sign_contract.view_model.SignContractViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import com.luminsoft.enroll_sdk.ui_components.theme.appColors
import org.koin.compose.koinInject


@Composable
fun CurrentContractLowRiskFRAScreenContent(
    signContractViewModel: SignContractViewModel,
    navController: NavController,
) {

    val getCurrentContractLowRiskFRAUseCase = GetCurrentContractLowRiskFRAUseCase(koinInject())
    val getSignContractFileLowRiskFRAUseCase = GetSignContractFileLowRiskFRAUseCase(koinInject())
    val context = LocalContext.current

    val contractFileModelList = signContractViewModel.contractFileModelList.collectAsState()

    val currentContractLowRiskFRAViewModel =
        remember {
            CurrentContractLowRiskFRAViewModel(
                getCurrentContractLowRiskFRAUseCase = getCurrentContractLowRiskFRAUseCase,
                getSignContractFileLowRiskFRAUseCase = getSignContractFileLowRiskFRAUseCase,
                context = context,
                contractId = signContractViewModel.contractId.value!!,
                contractVersionNumber = signContractViewModel.contractVersionNumber.value!!,
                currentText = contractFileModelList.value!![0].signContractTextEnum.toString()
            )
        }
    val currentContractLowRiskFRAVM = remember { currentContractLowRiskFRAViewModel }


    val activity = context.findActivity()
    val loading = currentContractLowRiskFRAVM.loading.collectAsState()
    val failure = currentContractLowRiskFRAVM.failure.collectAsState()
    val bitmap = currentContractLowRiskFRAVM.bitmap.collectAsState()
    val currentStepIndex = signContractViewModel.currentStepIndex.collectAsState()
    val getCurrentContract = signContractViewModel.getCurrentContract.collectAsState()
    val showAllContracts = signContractViewModel.showAllContracts.collectAsState()
    val showDialog = remember { mutableStateOf(false) }


    LaunchedEffect(currentStepIndex.value) {
        if (getCurrentContract.value)
            currentContractLowRiskFRAVM.callGetCurrentContract(xCurrentText = signContractViewModel.getContractText())
    }

    LaunchedEffect(showAllContracts.value) {
        if (showAllContracts.value)
            currentContractLowRiskFRAVM.callGetSignContractFile()
    }
    BackGroundView(navController = navController, showAppBar = true) {

        if (showDialog.value) {
            DialogView(
                bottomSheetStatus = BottomSheetStatus.SUCCESS,
                text = stringResource(id = R.string.successfulRegistration),
                buttonText = stringResource(id = R.string.continue_to_next),
                onPressedButton = {
                    activity.finish()
                    EnrollSDK.enrollCallback?.success(
                        EnrollSuccessModel(
                            activity.getString(R.string.successfulAuthentication),
//                            signContractViewModel.documentId.value,
//                            signContractViewModel.applicantId.value,
                        )
                    )
                }
            )
        }
        if (loading.value) LoadingView()
        else if (!failure.value?.message.isNullOrEmpty()) {
            if (failure.value is AuthFailure) {
                failure.value?.let {
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                        },
                    ) {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                    }
                }
            } else {
                failure.value?.let {
                    DialogView(bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.retry),
                        onPressedButton = {
                            // to determine which api is failed
//                            if (!termsIdReceived.value) {
//                                currentContractLowRiskFRAVM.getTermsId()
//                            } else if (!pdfFileReceived.value) {
//                                currentContractLowRiskFRAVM.getTermsPdfFileById()
//                            } else {
//                                currentContractLowRiskFRAVM.acceptTerms()
//                            }

                        },
                        secondButtonText = stringResource(id = R.string.exit),
                        onPressedSecondButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                        }) {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                    }
                }
            }
        } else {
            PdfViewerWidget(
                bitmap.value!!,
                activity = activity,
                contractFileModelList = contractFileModelList.value!!,
                currentStepIndex = currentStepIndex.value,
                showAllContracts = showAllContracts.value,
                currentContractLowRiskFRAVM = currentContractLowRiskFRAVM,
                context = context,
                onAcceptClick = {
                    signContractViewModel.getNextContract()
                }, onSignClick = {
                    navController.navigate(signContractScreenContent)
                }
            )
        }
    }
}


@Composable
fun PdfViewerWidget(
    bitmaps: List<Bitmap>,
    onAcceptClick: () -> Unit,
    onSignClick: () -> Unit,
    activity: Activity,
    contractFileModelList: ArrayList<ContractFileModel>,
    currentStepIndex: Int,
    showAllContracts: Boolean,
    currentContractLowRiskFRAVM: CurrentContractLowRiskFRAViewModel,
    context: Context
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (!showAllContracts)
                PDFHeader(
                    contractFileModelList,
                    currentStepIndex,
                    currentContractLowRiskFRAVM,
                    context
                )
            else
                DownloadIcon(currentContractLowRiskFRAVM, context, "final")
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.55f)
                    .shadow(8.dp, shape = RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(12.dp)

            ) {

                LazyColumn(
                    modifier = Modifier
                ) {
                    items(bitmaps.size) { index ->
                        Image(
                            bitmap = bitmaps[index].asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                if (!showAllContracts)
                    ButtonView(
                        onClick = onAcceptClick,
                        title = stringResource(id = R.string.approve)
                    )
                else
                    ButtonView(
                        onClick = onSignClick,
                        title = stringResource(id = R.string.sign)
                    )
                Spacer(modifier = Modifier.height(8.dp))

                ButtonView(
                    onClick = { showConfirmationDialog = true },
                    title = stringResource(id = R.string.exit),
                    color = MaterialTheme.appColors.backGround,
                    borderColor = MaterialTheme.appColors.primary,
                    textColor = MaterialTheme.appColors.primary,
                )

            }
        }

        if (showConfirmationDialog) {
            DialogView(
                bottomSheetStatus = BottomSheetStatus.WARNING,
                text = stringResource(id = R.string.exitConfirmation),
                buttonText = stringResource(id = R.string.exit),
                onPressedButton = {

                    showConfirmationDialog = false

                    activity.finish()
                    EnrollSDK.enrollCallback?.error(
                        EnrollFailedModel(
                            "Press Exit Button",
                            Throwable()
                        )
                    )
                },
                secondButtonText = stringResource(id = R.string.cancel),
                onPressedSecondButton = {

                    showConfirmationDialog = false
                }
            )
        }
    }
}

@Composable
private fun DownloadIcon(
    currentContractLowRiskFRAVM: CurrentContractLowRiskFRAViewModel,
    context: Context, fileName: String
) {
    Image(
        painterResource(R.drawable.download_icon),
        contentDescription = "download icon",
        modifier = Modifier
            .height(50.dp)
            .padding(end = 10.dp)
            .clickable {
                currentContractLowRiskFRAVM.downloadPDF(context = context, fileName = fileName)
            }
    )
}

@Composable
private fun PDFHeader(
    contractFileModelList: ArrayList<ContractFileModel>,
    currentStepIndex: Int,
    currentContractLowRiskFRAVM: CurrentContractLowRiskFRAViewModel,
    context: Context
) {
    val shape = RoundedCornerShape(8.dp)
    val squareShape = RoundedCornerShape(4.dp) // Optional: small rounding

    Row(
        modifier = Modifier
            .shadow(
                elevation = 6.dp,
                shape = shape,
                clip = false // Important: if true, auto-applies clip; if false, use clip() manually
            )
            .clip(shape)
            .border(
                width = 1.dp,
                color = lightenColor(MaterialTheme.appColors.primary, 0.4f),
                shape = shape // Optional
            )
            .background(
                color = lightenColor(MaterialTheme.appColors.primary, 0.55f),
                shape = shape
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyRow(
            modifier = Modifier
                .padding(start = 10.dp)
        ) {
            items(contractFileModelList.size) { index ->
                if (index == currentStepIndex)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(24.dp) // Square box: width = height
                            .background(MaterialTheme.appColors.primary, shape = squareShape)
                            .padding(horizontal = 5.dp)
                    ) {
                        Text(
                            text = contractFileModelList[index].sectionOrder!!.toString(),
                            color = Color.White, // White text
                            fontSize = 14.sp,     // Smaller text
                        )
                    }
                else
                    Text(
                        text = contractFileModelList[index].sectionOrder!!.toString(),
                        color = MaterialTheme.appColors.appBlack,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                    )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        DownloadIcon(currentContractLowRiskFRAVM, context, currentStepIndex.toString())
    }
}

