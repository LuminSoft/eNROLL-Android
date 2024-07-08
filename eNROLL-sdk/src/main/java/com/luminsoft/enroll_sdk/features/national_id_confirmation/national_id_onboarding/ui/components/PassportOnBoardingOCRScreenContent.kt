package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.CustomerData
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationApproveUseCase
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationUploadImageUseCase
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingErrorScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.passportOnBoardingConfirmationScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.view_model.PassportOcrViewModel
import com.luminsoft.enroll_sdk.innovitices.activities.DocumentActivity
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.NormalTextField
import com.luminsoft.enroll_sdk.ui_components.components.SpinKitLoadingIndicator
import org.koin.compose.koinInject

var userNameArValue = mutableStateOf(TextFieldValue())

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PassportOnBoardingConfirmationScreen(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val document = onBoardingViewModel.passportImage.collectAsState()

    val personalConfirmationUploadImageUseCase =
        PersonalConfirmationUploadImageUseCase(koinInject())

    val personalConfirmationApproveUseCase =
        PersonalConfirmationApproveUseCase(koinInject())

    val passportOcrVM =
        document.value?.let {
            remember {
                PassportOcrViewModel(
                    personalConfirmationUploadImageUseCase,
                    personalConfirmationApproveUseCase,
                    it
                )
            }
        }


    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val documentFrontUri = it.data?.data
            if (documentFrontUri != null) {
                try {
                    onBoardingViewModel.enableLoading()
                    val facialDocumentModel =
                        DotHelper.documentNonFacial(documentFrontUri, activity)
                    onBoardingViewModel.passportImage.value =
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


    MainContent(
        navController,
        passportOcrVM!!,
        activity,
        startForResult,
        onBoardingViewModel
    )
}

@Composable
private fun MainContent(
    navController: NavController,
    passportOcrVM: PassportOcrViewModel,
    activity: Activity,
    startForResult: ManagedActivityResultLauncher<Intent, ActivityResult>,
    onBoardingViewModel: OnBoardingViewModel,
) {
    val passportOcrVMOcrViewModel = remember { passportOcrVM }

    val customerData = passportOcrVMOcrViewModel.customerData.collectAsState()
    val loading = passportOcrVMOcrViewModel.loading.collectAsState()
    val passportApproved = passportOcrVMOcrViewModel.passportApproved.collectAsState()
    val failure = passportOcrVMOcrViewModel.failure.collectAsState()
    val userHasModifiedText = remember { mutableStateOf(false) }

    BackGroundView(navController = navController, showAppBar = true) {
        if (passportApproved.value) {
            val isEmpty = onBoardingViewModel.removeCurrentStep(1)
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
        if (loading.value)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { SpinKitLoadingIndicator() }
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
                    )
                    {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                    }
                }
            } else {
                failure.value?.let {
                    val msg: String = if (it.message == "Object reference not set to an instance of an object.")
                        stringResource(id = R.string.someThingWentWrong)
                    else
                        it.message

                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = msg,
                        buttonText = stringResource(id = R.string.retry),
                        onPressedButton = {
                            passportOcrVMOcrViewModel.resetFailure()
                            onBoardingViewModel.enableLoading()
                            val intent =
                                Intent(activity.applicationContext, DocumentActivity::class.java)
                            intent.putExtra("scanType", DocumentActivity().PASSPORT_SCAN)
                            intent.putExtra("localCode", EnrollSDK.localizationCode.name)
                            startForResult.launch(intent)
                        },
                        secondButtonText = stringResource(id = R.string.exit),
                        onPressedSecondButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                        }
                    )
                    {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                    }
                }
            }
        } else if (customerData.value != null) {
            if (customerData.value!!.fullNameAr != null)
                if (!userHasModifiedText.value) {
                    userNameArValue.value = TextFieldValue(customerData.value!!.fullNameAr!!)
                }
            setCustomerId(onBoardingViewModel, customerData)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.72f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    TextItem(
                        R.string.nameEn,
                        customerData.value!!.fullNameEn!!,
                        R.drawable.user_icon,
                        80.0
                    )
                    if (customerData.value!!.fullNameAr != null) Spacer(
                        modifier = Modifier.height(
                            10.dp
                        )
                    )

                    if (customerData.value!!.fullNameAr != null)
                        NormalTextField(
                            label = ResourceProvider.instance.getStringResource(R.string.nameAr),
                            value = userNameArValue.value,
                            height = 60.0,
                            icon = {
                                Image(
                                    painterResource(R.drawable.user_icon),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .height(50.dp)
                                )
                            },
                            trailingIcon = {
                                Image(
                                    painterResource(R.drawable.edit_icon),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .height(50.dp)
                                )
                            },
                            onValueChange = {
                                userNameArValue.value = it
                                userHasModifiedText.value = true
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                            ),
                            error = arabicNameValidation(),
                        )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextItem(
                        R.string.gender,
                        customerData.value!!.gender!!,
                        R.drawable.gender_icon,
                        60.0
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextItem(
                        R.string.birthDate,
                        customerData.value!!.birthdate!!.split("T")[0],
                        R.drawable.calendar_icon,
                        60.0
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextItem(
                        R.string.passportDocumentNumber,
                        customerData.value!!.documentNumber!!,
                        R.drawable.passport_icon,
                        60.0
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextItem(
                        R.string.dateOfExpiry,
                        customerData.value!!.expirationDate!!.split("T")[0],
                        R.drawable.calendar_icon,
                        60.0
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextItem(
                        R.string.issuingAuthority,
                        customerData.value!!.issuingAuthority!!,
                        R.drawable.issuing_authurity_icon,
                        60.0
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextItem(
                        R.string.nationality,
                        customerData.value!!.nationality!!,
                        R.drawable.nationality_icon,
                        60.0
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextItem(
                        R.string.documentCode,
                        customerData.value!!.documentCode!!,
                        R.drawable.factory_num_icon,
                        60.0
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextItem(
                        R.string.visualZone,
                        customerData.value!!.visualZone!!,
                        R.drawable.factory_num_icon,
                        120.0
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))

                Column {
                    ButtonView(
                        onClick = {
                            onBoardingViewModel.enableLoading()
                            if (customerData.value!!.fullNameAr != null && arabicNameValidation() == null)
                                passportOcrVMOcrViewModel.callApproveFront(userNameArValue.value.text)
                            else if (customerData.value!!.fullNameAr == null)
                                passportOcrVMOcrViewModel.callApproveFront("")

                        },
                        title = stringResource(id = R.string.confirmAndContinue)
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    ButtonView(
                        onClick = {
                            onBoardingViewModel.enableLoading()
                            val intent =
                                Intent(activity.applicationContext, DocumentActivity::class.java)
                            intent.putExtra("scanType", DocumentActivity().PASSPORT_SCAN)
                            intent.putExtra("localCode", EnrollSDK.localizationCode.name)
                            startForResult.launch(intent)
                        },
                        textColor = MaterialTheme.colorScheme.primary,
                        color = MaterialTheme.colorScheme.onPrimary,
                        borderColor = MaterialTheme.colorScheme.primary,
                        title = stringResource(id = R.string.reScan)
                    )
                    Spacer(modifier = Modifier.height(100.dp))
                }

            }
        }

    }
}


private fun setCustomerId(
    onBoardingViewModel: OnBoardingViewModel,
    customerData: State<CustomerData?>
) {
    onBoardingViewModel.customerId.value = "1111"
//    onBoardingViewModel.customerId.value = customerData.value?.customerId
    onBoardingViewModel.facePhotoPath.value = customerData.value?.photo
}

@Composable
private fun TextItem(label: Int, value: String, icon: Int, height: Double) {
    val newValue: String = if (label == R.string.gender) {
        if (value == "M") ResourceProvider.instance.getStringResource(R.string.male)
        else ResourceProvider.instance.getStringResource(R.string.female)
    } else value


    NormalTextField(label = ResourceProvider.instance.getStringResource(label),
        value = TextFieldValue(text = newValue),
        onValueChange = { },
        height = height,
        enabled = false,
        singleLine = false,
        icon = {
            Image(
                painterResource(icon), contentDescription = "", modifier = Modifier.height(50.dp)
            )
        })
}


private fun arabicNameValidation() = when {

    userNameArValue.value.text.isEmpty() -> {
        ResourceProvider.instance.getStringResource(R.string.required_arabic_name)
    }

    userNameArValue.value.text.length < 2 -> {
        ResourceProvider.instance.getStringResource(R.string.invalid_arabic_name_min)
    }

    userNameArValue.value.text.length > 150 -> {
        ResourceProvider.instance.getStringResource(R.string.invalid_arabic_name_max)
    }

    !Regex("^[\\u0621-\\u064A-. ]+\$").matches(
        userNameArValue.value.text
    ) -> {
        ResourceProvider.instance.getStringResource(R.string.invalid_arabic_name)

    }

    !userNameArValue.value.text.trim().contains(" ") -> {
        ResourceProvider.instance.getStringResource(R.string.arabic_name_must_contain_space)
    }

    else -> null
}
