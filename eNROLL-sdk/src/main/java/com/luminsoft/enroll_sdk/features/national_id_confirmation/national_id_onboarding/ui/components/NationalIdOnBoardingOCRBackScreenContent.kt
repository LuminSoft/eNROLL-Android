package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import appColors
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.failures.NIFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationApproveUseCase
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationUploadImageUseCase
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingBackConfirmationScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingErrorScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingPreScanScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.view_model.NationalIdBackOcrViewModel
import com.luminsoft.enroll_sdk.innovitices.activities.DocumentActivity
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.EkycStepType
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.NormalTextField
import com.luminsoft.enroll_sdk.ui_components.components.SpinKitLoadingIndicator
import org.koin.compose.koinInject


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NationalIdOnBoardingBackConfirmationScreen(
    navController: NavController, onBoardingViewModel: OnBoardingViewModel
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val document = onBoardingViewModel.nationalIdBackImage.collectAsState()


    val personalConfirmationUploadImageUseCase =
        PersonalConfirmationUploadImageUseCase(koinInject())

    val personalConfirmationApproveUseCase = PersonalConfirmationApproveUseCase(koinInject())

    val nationalIdBackOcrVM = document.value?.let {
        remember {
            onBoardingViewModel.customerId.value?.let { it1 ->
                NationalIdBackOcrViewModel(
                    personalConfirmationUploadImageUseCase,
                    personalConfirmationApproveUseCase,
                    it,
                    it1
                )
            }
        }
    }

    val nationalIdBackOcrViewModel = remember { nationalIdBackOcrVM }

    val customerData = nationalIdBackOcrViewModel!!.customerData.collectAsState()
    val loading = nationalIdBackOcrViewModel.loading.collectAsState()
    val failure = nationalIdBackOcrViewModel.failure.collectAsState()
    val backNIApproved = nationalIdBackOcrViewModel.backNIApproved.collectAsState()
    val isPassportAndMail = onBoardingViewModel.isPassportAndMail.collectAsState()


    val startForBackResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val documentBackUri = it.data?.data
            if (documentBackUri != null) {
                try {
                    onBoardingViewModel.disableLoading()
                    val nonFacialDocumentModel =
                        DotHelper.documentNonFacial(documentBackUri, activity)
                    onBoardingViewModel.nationalIdBackImage.value =
                        nonFacialDocumentModel.documentImageBase64
                    navController.navigate(nationalIdOnBoardingBackConfirmationScreen)
                } catch (e: Exception) {
                    onBoardingViewModel.disableLoading()
                    onBoardingViewModel.errorMessage.value = e.message
                    onBoardingViewModel.scanType.value = ScanType.Back
                    navController.navigate(nationalIdOnBoardingErrorScreen)
                    println(e.message)
                }
            } else if (it.resultCode == 19 || it.resultCode == 8) {
                onBoardingViewModel.disableLoading()
                onBoardingViewModel.errorMessage.value =
                    context.getString(R.string.timeoutException)
                onBoardingViewModel.scanType.value = ScanType.Back
                navController.navigate(nationalIdOnBoardingErrorScreen)
            }
        }


    BackGroundView(navController = navController, showAppBar = true) {
        if (backNIApproved.value) {
            if (isPassportAndMail.value) {
                onBoardingViewModel.disableLoading()
                onBoardingViewModel.isPassportAndMailFinal.value = true
                navController.navigate(nationalIdOnBoardingPreScanScreen)
            } else {
                val isEmpty = onBoardingViewModel.removeCurrentStep(EkycStepType.PersonalConfirmation.getStepId())
                if (isEmpty)
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.SUCCESS,
                        text = stringResource(id = R.string.successfulRegistration),
                        buttonText = stringResource(id = R.string.continue_to_next),
                        onPressedButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(
                                EnrollFailedModel(
                                    activity.getString(R.string.successfulRegistration),
                                    activity.getString(R.string.successfulRegistration)
                                )
                            )
                        },
                    )
            }
        }

        if (loading.value) Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) { SpinKitLoadingIndicator() }
        else if (customerData.value != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                if (customerData.value!!.profession != null) TextItem(
                    R.string.profession,
                    customerData.value!!.profession!!,
                    R.drawable.profession_icon
                )
                if (customerData.value!!.gender != null) Spacer(modifier = Modifier.height(10.dp))
                if (customerData.value!!.gender != null) TextItem(
                    R.string.gender, customerData.value!!.gender!!, R.drawable.gender_icon
                )
                if (customerData.value!!.religion != null) Spacer(modifier = Modifier.height(10.dp))
                if (customerData.value!!.religion != null) TextItem(
                    R.string.religion, customerData.value!!.religion!!, R.drawable.religion_icon
                )
                if (customerData.value!!.expirationDate != null) Spacer(
                    modifier = Modifier.height(
                        10.dp
                    )
                )
                if (customerData.value!!.expirationDate != null) TextItem(
                    R.string.dateOfExpiry,
                    customerData.value!!.expirationDate!!.split("T")[0],
                    R.drawable.calendar_icon
                )
                if (customerData.value!!.maritalStatus != null) Spacer(
                    modifier = Modifier.height(
                        10.dp
                    )
                )
                if (customerData.value!!.maritalStatus != null) TextItem(
                    R.string.maritalStatus,
                    customerData.value!!.maritalStatus!!,
                    R.drawable.marital_status_icon
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.3f))

                ButtonView(
                    onClick = {
                        onBoardingViewModel.enableLoading()
                        nationalIdBackOcrViewModel.callApproveBack()
                    }, title = stringResource(id = R.string.confirmAndContinue)
                )
                Spacer(modifier = Modifier.height(10.dp))

                ButtonView(
                    onClick = {
                        onBoardingViewModel.enableLoading()
                        val intent =
                            Intent(activity.applicationContext, DocumentActivity::class.java)
                        intent.putExtra("scanType", DocumentActivity().BACK_SCAN)
                        intent.putExtra("localCode", EnrollSDK.localizationCode.name)

                        startForBackResult.launch(intent)
                    },
                    textColor = MaterialTheme.appColors.primary,
                    color = MaterialTheme.appColors.backGround,
                    borderColor = MaterialTheme.appColors.primary,
                    title = stringResource(id = R.string.reScan)
                )
            }
        } else if (!failure.value?.message.isNullOrEmpty()) {
            when (failure.value) {
                is AuthFailure -> {
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
                }

                is NIFailure -> {
                    failure.value?.let {
                        DialogView(bottomSheetStatus = BottomSheetStatus.ERROR,
                            text = context.getString(it.strInt),
                            buttonText = stringResource(id = R.string.retry),
                            onPressedButton = {
                                nationalIdBackOcrViewModel.resetFailure()
                                onBoardingViewModel.enableLoading()
                                val intent =
                                    Intent(
                                        activity.applicationContext,
                                        DocumentActivity::class.java
                                    )
                                intent.putExtra("scanType", DocumentActivity().BACK_SCAN)
                                intent.putExtra("localCode", EnrollSDK.localizationCode.name)

                                startForBackResult.launch(intent)
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

                else -> {
                    failure.value?.let {
                        DialogView(bottomSheetStatus = BottomSheetStatus.ERROR,
                            text = it.message,
                            buttonText = stringResource(id = R.string.retry),
                            onPressedButton = {
                                nationalIdBackOcrViewModel.resetFailure()
                                onBoardingViewModel.enableLoading()
                                val intent =
                                    Intent(
                                        activity.applicationContext,
                                        DocumentActivity::class.java
                                    )
                                intent.putExtra("scanType", DocumentActivity().BACK_SCAN)
                                intent.putExtra("localCode", EnrollSDK.localizationCode.name)

                                startForBackResult.launch(intent)
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
            }
        }
    }
}

@Composable
private fun TextItem(label: Int, value: String, icon: Int) {
    val newValue: String = if (label == R.string.gender) {
        if (value == "M") ResourceProvider.instance.getStringResource(R.string.male)
        else ResourceProvider.instance.getStringResource(R.string.female)
    } else value

    NormalTextField(label = ResourceProvider.instance.getStringResource(label),
        value = TextFieldValue(text = newValue),
        onValueChange = { },
        height = 60.0,
        enabled = false,
        icon = {
            Image(
                painterResource(icon), contentDescription = "", modifier = Modifier.height(50.dp)
            )
        })
}
