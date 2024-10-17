package com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_onboarding.ui.components

import com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_domain.usecases.CheckUserHasNationalIdUseCase
import ElectronicSignatureOnBoardingViewModel
import InsertSignatureInfoUseCase
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import appColors
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.models.EnrollSuccessModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
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
fun ApplyForElectronicSignatureScreenContent(
    onBoardingViewModel: OnBoardingViewModel,
    navController: NavController,
) {

    val electronicSignatureUseCase =
        InsertSignatureInfoUseCase(koinInject())

    val checkUserHasNationalIdUseCase =
        CheckUserHasNationalIdUseCase(koinInject())

    val electronicSignatureOnBoardingViewModel =
        remember {
            ElectronicSignatureOnBoardingViewModel(
                electronicSignatureUseCase = electronicSignatureUseCase,
                checkUserHasNationalIdUseCase = checkUserHasNationalIdUseCase
            )
        }

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = electronicSignatureOnBoardingViewModel.loading.collectAsState()
    val failure = electronicSignatureOnBoardingViewModel.failure.collectAsState()
    val applySignatureSucceed = electronicSignatureOnBoardingViewModel.applySignatureSucceed.collectAsState()


    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogStatus by remember { mutableStateOf(BottomSheetStatus.SUCCESS) }
    var dialogButtonText by remember { mutableStateOf("") }
    var dialogOnPressButton: (() -> Unit)? by remember { mutableStateOf(null) }

    fun navigateToNextStep() {
        onBoardingViewModel.mailValue.value = TextFieldValue()
        onBoardingViewModel.currentPhoneNumber.value = null
        navController.navigate(onBoardingViewModel.steps.value!!.first().stepNameNavigator())
    }

    fun removeCurrentStep(id: Int): Boolean {
        if (onBoardingViewModel.steps.value != null) {
            val stepsSize = onBoardingViewModel.steps.value!!.size
            onBoardingViewModel.steps.value = onBoardingViewModel.steps.value!!.toMutableList().apply {
                removeIf { x -> x.registrationStepId == id }
            }.toList()
            val newStepsSize = onBoardingViewModel.steps.value!!.size
            if (stepsSize != newStepsSize) {
                return onBoardingViewModel.steps.value!!.isEmpty()
            }
        }
        return false
    }

    fun showSuccessDialog(isEmpty: Boolean) {
        dialogMessage = context.getString(R.string.we_will_contact_you_to_receive_the_physical_token)
        dialogButtonText = context.getString(R.string.continue_to_next)
        dialogStatus = BottomSheetStatus.SUCCESS
        dialogOnPressButton = {
            if (isEmpty) {
                activity.finish()
                EnrollSDK.enrollCallback?.success(
                    EnrollSuccessModel(
                        activity.getString(R.string.successfulAuthentication),
                        onBoardingViewModel.documentId.value
                    )
                )
            } else {
                navigateToNextStep()
                showDialog = false
            }
        }
        showDialog = true
    }

    LaunchedEffect(applySignatureSucceed.value) {
        applySignatureSucceed.value?.let { succeed ->
            if (succeed) {
                val isEmpty = removeCurrentStep(EkycStepType.ElectronicSignature.getStepId())
                showSuccessDialog(isEmpty)
            }
        }
    }

    BackGroundView(navController = navController, showAppBar = true) {
        if (showDialog) {
            dialogOnPressButton?.let {
                DialogView(
                    bottomSheetStatus = dialogStatus,
                    text = dialogMessage,
                    buttonText = dialogButtonText,
                    onPressedButton = it
                )
            }
        }
        if (loading.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { SpinKitLoadingIndicator() }
        } else if (!failure.value?.message.isNullOrEmpty()) {
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
                            showDialog = false
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


            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.5f))

                if (electronicSignatureOnBoardingViewModel.userHasNationalId.value == false) {
                    NationalIdTextField(electronicSignatureOnBoardingViewModel)
                    Spacer(modifier = Modifier.height(15.dp))
                }

                if (!onBoardingViewModel.existingSteps.value!!.contains(3)) {
                    PhoneNumberTextField(electronicSignatureOnBoardingViewModel)
                    Spacer(modifier = Modifier.height(15.dp))
                }


                if (!onBoardingViewModel.existingSteps.value!!.contains(4)) {
                    EmailTextField(electronicSignatureOnBoardingViewModel)

                }


                Spacer(modifier = Modifier.weight(1f))
                ButtonView(
                    onClick = {

                        var isValid = true

                        if (electronicSignatureOnBoardingViewModel.userHasNationalId.value == false) {
                            if (electronicSignatureOnBoardingViewModel.nationalIdValue.value.text.length != 14) {
                                electronicSignatureOnBoardingViewModel.nationalIdError.value =
                                    ResourceProvider.instance.getStringResource(R.string.emptyError)
                                isValid = false
                            }
                        }


                        if (!onBoardingViewModel.existingSteps.value!!.contains(4)) {

                            emailFormatValidation(electronicSignatureOnBoardingViewModel)

                            if (electronicSignatureOnBoardingViewModel.emailValue.value.text.isEmpty()) {
                                electronicSignatureOnBoardingViewModel.emailError.value =
                                    ResourceProvider.instance.getStringResource(R.string.emptyError)
                                isValid = false
                            } else if (!Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}").matches(electronicSignatureOnBoardingViewModel.emailValue.value.text)) {
                                electronicSignatureOnBoardingViewModel.emailError.value =
                                    ResourceProvider.instance.getStringResource(R.string.invalid_email)
                                isValid = false
                            }
                        }

                        if (!onBoardingViewModel.existingSteps.value!!.contains(3)) {
                            if (!Regex("^(\\+20|0)?1[0125][0-9]{8}\$").matches(electronicSignatureOnBoardingViewModel.phoneNumberValue.value.text)) {
                                electronicSignatureOnBoardingViewModel.phoneNumberError.value =
                                    ResourceProvider.instance.getStringResource(R.string.emptyError)
                                isValid = false
                            }
                        }

                        if (isValid) {
                            electronicSignatureOnBoardingViewModel.insertSignatureInfo(
                                2,
                                if (electronicSignatureOnBoardingViewModel.userHasNationalId.value == true) "" else electronicSignatureOnBoardingViewModel.nationalIdValue.value.text,
                                if (onBoardingViewModel.existingSteps.value!!.contains(3)) "" else electronicSignatureOnBoardingViewModel.phoneNumberValue.value.text,
                                if (onBoardingViewModel.existingSteps.value!!.contains(4)) "" else electronicSignatureOnBoardingViewModel.emailValue.value.text
                            )
                        }
                    },
                    stringResource(id = R.string.confirmAndContinue),
                )
                Spacer(modifier = Modifier.weight(1f))

            }
        }
    }
}


private fun emailFormatValidation(electronicSignatureOnBoardingViewModel: ElectronicSignatureOnBoardingViewModel) {
    when {

        electronicSignatureOnBoardingViewModel.emailValue.value.text.isEmpty() -> {
            electronicSignatureOnBoardingViewModel.emailError.value = null
        }

        !Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}").matches(
            electronicSignatureOnBoardingViewModel.emailValue.value.text
        ) -> {
            electronicSignatureOnBoardingViewModel.emailError.value =
                ResourceProvider.instance.getStringResource(R.string.invalid_email)
        }

        else -> electronicSignatureOnBoardingViewModel.emailError.value = null
    }
}

@Composable
fun NationalIdTextField(
    electronicSignatureOnBoardingViewModel: ElectronicSignatureOnBoardingViewModel
) {

    val nationalIdValue = electronicSignatureOnBoardingViewModel.nationalIdValue.collectAsState()

    NormalTextField(
        label = ResourceProvider.instance.getStringResource(R.string.type_your_national_id),
        value = nationalIdValue.value,
        height = 60.0,
        icon = {
            Image(
                painter = painterResource(R.drawable.id_card_icon),
                contentDescription = "",
                colorFilter =   ColorFilter.tint(MaterialTheme.appColors.primary),

                modifier = Modifier.height(50.dp)
            )
        },
        onValueChange = { newValue ->

            if (newValue.text.length > 14) return@NormalTextField
            electronicSignatureOnBoardingViewModel.nationalIdValue.value = newValue
            electronicSignatureOnBoardingViewModel.userHasModifiedText.value = true


            electronicSignatureOnBoardingViewModel.nationalIdError.value =
                if (newValue.text.length != 14) {
                    ResourceProvider.instance.getStringResource(R.string.invalid_national_id)
                } else {
                    null
                }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        error = electronicSignatureOnBoardingViewModel.nationalIdError.value
    )
}


@Composable
fun PhoneNumberTextField(
    electronicSignatureOnBoardingViewModel: ElectronicSignatureOnBoardingViewModel
) {
    val egyptianPhoneNumberRegex = Regex("^(\\+20|0)?1[0125][0-9]{8}\$")
    val phoneNumberValue = electronicSignatureOnBoardingViewModel.phoneNumberValue.collectAsState()

    NormalTextField(
        label = ResourceProvider.instance.getStringResource(R.string.type_your_phoneNumber),
        value = phoneNumberValue.value,
        height = 60.0,
        icon = {
            Image(
                painter = painterResource(R.drawable.factory_num_icon),
                contentDescription = "",
                colorFilter =   ColorFilter.tint(MaterialTheme.appColors.primary),
                modifier = Modifier.height(50.dp)
            )
        },
        onValueChange = { newValue ->

            electronicSignatureOnBoardingViewModel.phoneNumberValue.value = newValue
            electronicSignatureOnBoardingViewModel.userHasModifiedText.value = true


            electronicSignatureOnBoardingViewModel.phoneNumberError.value =
                if (!egyptianPhoneNumberRegex.matches(newValue.text)) {
                    ResourceProvider.instance.getStringResource(R.string.invalid_phone_number)
                } else {
                    null
                }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        error = electronicSignatureOnBoardingViewModel.phoneNumberError.value
    )
}


@Composable
fun EmailTextField(
    electronicSignatureOnBoardingViewModel: ElectronicSignatureOnBoardingViewModel
) {

    val mailValue = electronicSignatureOnBoardingViewModel.emailValue.collectAsState()

    NormalTextField(
        label = ResourceProvider.instance.getStringResource(R.string.type_your_email),
        value = mailValue.value,
        height = 60.0,
        icon = {
            Image(
                painterResource(R.drawable.mail_icon),
                contentDescription = "",
                colorFilter =   ColorFilter.tint(MaterialTheme.appColors.primary),
                modifier = Modifier
                    .height(50.dp)
            )
        },
        onValueChange = {
            electronicSignatureOnBoardingViewModel.emailValue.value = it
            emailFormatValidation(electronicSignatureOnBoardingViewModel)
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Email
        ),
        error = electronicSignatureOnBoardingViewModel.emailError.value,
    )


}


