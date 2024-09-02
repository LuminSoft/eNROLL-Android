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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import appColors
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.CustomerData
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationApproveUseCase
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationUploadImageUseCase
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingBackConfirmationScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingErrorScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingFrontConfirmationScreen
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.view_model.NationalIdFrontOcrViewModel
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

var userNameValue = mutableStateOf(TextFieldValue())

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NationalIdOnBoardingFrontConfirmationScreen(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val document = onBoardingViewModel.nationalIdFrontImage.collectAsState()

    val personalConfirmationUploadImageUseCase =
        PersonalConfirmationUploadImageUseCase(koinInject())

    val personalConfirmationApproveUseCase =
        PersonalConfirmationApproveUseCase(koinInject())

    val nationalIdFrontOcrVM =
        document.value?.let {
            remember {
                NationalIdFrontOcrViewModel(
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
                    onBoardingViewModel.nationalIdFrontImage.value =
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

    val startForBackResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val documentBackUri = it.data?.data
            if (documentBackUri != null) {
                try {
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



    MainContent(
        navController,
        nationalIdFrontOcrVM!!,
        activity,
        startForBackResult,
        startForResult,
        onBoardingViewModel
    )
}

@Composable
private fun MainContent(
    navController: NavController,
    nationalIdFrontOcrVM: NationalIdFrontOcrViewModel,
    activity: Activity,
    startForBackResult: ManagedActivityResultLauncher<Intent, ActivityResult>,
    startForResult: ManagedActivityResultLauncher<Intent, ActivityResult>,
    onBoardingViewModel: OnBoardingViewModel,
) {
    val nationalIdFrontOcrViewModel = remember { nationalIdFrontOcrVM }

    val customerData = nationalIdFrontOcrViewModel.customerData.collectAsState()
    val loading = nationalIdFrontOcrViewModel.loading.collectAsState()
    val frontNIApproved = nationalIdFrontOcrViewModel.frontNIApproved.collectAsState()
    val failure = nationalIdFrontOcrViewModel.failure.collectAsState()
    val userHasModifiedText = remember { mutableStateOf(false) }

    BackGroundView(navController = navController, showAppBar = true) {
        if (frontNIApproved.value) {
            val intent =
                Intent(activity.applicationContext, DocumentActivity::class.java)
            intent.putExtra("scanType", DocumentActivity().backScan)
            intent.putExtra("localCode", EnrollSDK.localizationCode.name)
            startForBackResult.launch(intent)
            nationalIdFrontOcrViewModel.scanBack()
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
                    val msg: String =
                        if (it.message == "Object reference not set to an instance of an object.")
                            stringResource(id = R.string.someThingWentWrong)
                        else
                            it.message
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = msg,
                        buttonText = stringResource(id = R.string.retry),
                        onPressedButton = {
                            nationalIdFrontOcrViewModel.resetFailure()
                            onBoardingViewModel.enableLoading()
                            val intent =
                                Intent(activity.applicationContext, DocumentActivity::class.java)
                            intent.putExtra("scanType", DocumentActivity().frontScan)
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
            if (customerData.value!!.fullNameEn != null)
                if (!userHasModifiedText.value) {
                    userNameValue.value = TextFieldValue(customerData.value!!.fullNameEn!!)
                }
            setCustomerId(onBoardingViewModel, customerData)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                TextItem(R.string.nameAr, customerData.value!!.fullName!!, R.drawable.user_icon)
                if (customerData.value!!.fullNameEn != null) Spacer(modifier = Modifier.height(10.dp))

                if (customerData.value!!.fullNameEn != null)
                    NormalTextField(
                        label = ResourceProvider.instance.getStringResource(R.string.nameEn),
                        value = userNameValue.value,
                        height = 60.0,
                        icon = {
                            Image(
                                painterResource(R.drawable.user_icon),
                                contentDescription = "",
                                colorFilter =   ColorFilter.tint(MaterialTheme.appColors.primary),
                                modifier = Modifier
                                    .height(50.dp)
                            )
                        },
                        trailingIcon = {
                            Image(
                                painterResource(R.drawable.edit_icon),
                                contentDescription = "",
                                colorFilter =   ColorFilter.tint(MaterialTheme.appColors.primary),

                                modifier = Modifier
                                    .height(50.dp)
                            )
                        },
                        onValueChange = {
                            userNameValue.value = it
                            userHasModifiedText.value = true
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                        ),
                        error = englishNameValidation(),
                    )
                if (customerData.value!!.address != null) Spacer(modifier = Modifier.height(10.dp))
                if (customerData.value!!.address != null) TextItem(
                    R.string.address,
                    customerData.value!!.address!!,
                    R.drawable.address_icon
                )
                if (customerData.value!!.state != null) Spacer(modifier = Modifier.height(10.dp))
                if (customerData.value!!.state != null) TextItem(
                    R.string.state,
                    customerData.value!!.state!!,
                    R.drawable.address_icon
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextItem(
                    R.string.birthDate,
                    customerData.value!!.birthdate!!.split("T")[0],
                    R.drawable.calendar_icon
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextItem(
                    R.string.nationalIdNumber,
                    customerData.value!!.idNumber!!,
                    R.drawable.id_card_icon
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextItem(
                    R.string.factoryNumber,
                    customerData.value!!.documentNumber!!,
                    R.drawable.factory_num_icon
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.3f))

                ButtonView(
                    onClick = {
                        onBoardingViewModel.enableLoading()
                        if (customerData.value!!.fullNameEn != null && englishNameValidation() == null)
                            nationalIdFrontOcrViewModel.callApproveFront(userNameValue.value.text)
                        else if (customerData.value!!.fullNameEn == null)
                            nationalIdFrontOcrViewModel.callApproveFront("")

                    },
                    title = stringResource(id = R.string.confirmAndContinue)
                )
                Spacer(modifier = Modifier.height(10.dp))

                ButtonView(
                    onClick = {
                        onBoardingViewModel.enableLoading()
                        val intent =
                            Intent(activity.applicationContext, DocumentActivity::class.java)
                        intent.putExtra("scanType", DocumentActivity().frontScan)
                        intent.putExtra("localCode", EnrollSDK.localizationCode.name)
                        startForResult.launch(intent)
                    },
                    title = stringResource(id = R.string.reScan),
                    color = MaterialTheme.appColors.backGround,
                    borderColor = MaterialTheme.appColors.primary,
                    textColor = MaterialTheme.appColors.primary
                )
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
private fun TextItem(label: Int, value: String, icon: Int) {
    NormalTextField(
        label = ResourceProvider.instance.getStringResource(label),
        value = TextFieldValue(text = value),
        onValueChange = { },
        enabled = false,
        height = 60.0,
        icon = {
            Image(
                painterResource(icon),
                contentDescription = "",
                modifier = Modifier
                    .height(50.dp)
            )
        }
    )
}


private fun englishNameValidation() = when {

    userNameValue.value.text.isEmpty() -> {
        ResourceProvider.instance.getStringResource(R.string.required_english_name)
    }

    userNameValue.value.text.length < 2 -> {
        ResourceProvider.instance.getStringResource(R.string.invalid_english_name_min)
    }

    userNameValue.value.text.length > 150 -> {
        ResourceProvider.instance.getStringResource(R.string.invalid_english_name_max)
    }

    !Regex("^[A-Za-z-. ]+\$").matches(
        userNameValue.value.text
    ) -> {
        ResourceProvider.instance.getStringResource(R.string.invalid_english_name)

    }

    else -> null
}
