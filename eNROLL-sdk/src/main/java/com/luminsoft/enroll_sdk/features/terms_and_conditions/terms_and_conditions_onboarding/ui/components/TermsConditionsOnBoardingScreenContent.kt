package com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_onboarding.ui.components

import GetTermsIdUseCase
import GetTermsPdfFileByIdUseCase
import TermsConditionsOnBoardingViewModel
import android.app.Activity
import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luminsoft.enroll_sdk.ui_components.theme.appColors
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.EnrollSuccessModel
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_domain.usecases.AcceptTermsUseCase
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.EkycStepType
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import org.koin.compose.koinInject


@Composable
fun TermsConditionsOnBoardingScreenContent(
    onBoardingViewModel: OnBoardingViewModel,
    navController: NavController,
) {

    val getTermsIdUseCase = GetTermsIdUseCase(koinInject())
    val getTermsPdfFileByIdUseCase = GetTermsPdfFileByIdUseCase(koinInject())
    val acceptTermsUseCase = AcceptTermsUseCase(koinInject())
    val context = LocalContext.current

    val termsConditionsOnBoardingViewModel =
        remember {
            TermsConditionsOnBoardingViewModel(
                getTermsIdUseCase = getTermsIdUseCase,
                getTermsPdfFileByIdUseCase = getTermsPdfFileByIdUseCase,
                acceptTermsUseCase = acceptTermsUseCase,
                context = context
            )
        }
    val termsConditionsOnBoardingVM = remember { termsConditionsOnBoardingViewModel }


    val activity = context.findActivity()
    val loading = termsConditionsOnBoardingVM.loading.collectAsState()
    val failure = termsConditionsOnBoardingVM.failure.collectAsState()
    val bitmap = termsConditionsOnBoardingVM.bitmap.collectAsState()
    val termsIdReceived = termsConditionsOnBoardingVM.termsIdReceived.collectAsState()
    val pdfFileReceived = termsConditionsOnBoardingVM.termsPdfReceived.collectAsState()
    val termsAccepted = termsConditionsOnBoardingVM.termsAccepted.collectAsState()
    val showDialog = remember { mutableStateOf(false) }



    BackGroundView(navController = navController, showAppBar = false) {
        if (termsAccepted.value) {

            val isEmpty =
                onBoardingViewModel.removeCurrentStep(EkycStepType.TermsConditions.getStepId())

            if (isEmpty) {
                LaunchedEffect(Unit) {
                    val apiResponse = onBoardingViewModel.getApplicantId()
                    apiResponse.fold(
                        {},
                        { _ -> showDialog.value = true }
                    )
                }
            }

        }
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
                            onBoardingViewModel.documentId.value,
                            onBoardingViewModel.applicantId.value,
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
                            if (!termsIdReceived.value) {
                                termsConditionsOnBoardingVM.getTermsId()
                            } else if (!pdfFileReceived.value) {
                                termsConditionsOnBoardingVM.getTermsPdfFileById()
                            } else {
                                termsConditionsOnBoardingVM.acceptTerms()
                            }

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
            PdfViewerWidget(bitmap.value!!, onAcceptClick = {
                termsConditionsOnBoardingVM.acceptTerms()
            }, activity = activity)
        }
    }
}


@Composable
fun PdfViewerWidget(
    bitmaps: List<Bitmap>, onAcceptClick: () -> Unit,
    activity: Activity

) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(id = R.string.readTermsAndConditions),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.appColors.textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            HorizontalDivider(
                modifier = Modifier
                    .width(50.dp)
                    .align(Alignment.CenterHorizontally),
                thickness = 4.dp,
                color = MaterialTheme.appColors.primary
            )


            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.6f)
                    .border(BorderStroke(1.dp, MaterialTheme.appColors.primary))
                    .padding(8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(bitmaps.size) { index ->
                        Image(
                            bitmap = bitmaps[index].asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
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
                ButtonView(
                    onClick = onAcceptClick,
                    title = stringResource(id = R.string.acceptTermsAndConditions)
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
                text = stringResource(id = R.string.youMustApproveOurTermsToProceed),
                buttonText = stringResource(id = R.string.exit),
                onPressedButton = {

                    showConfirmationDialog = false

                    activity.finish()
                    EnrollSDK.enrollCallback?.error(
                        EnrollFailedModel(
                            "Didn't accept our terms",
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

