package com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.core.failures.AuthFailure
import com.luminsoft.ekyc_android_sdk.core.models.PaymentFailedModel
import com.luminsoft.ekyc_android_sdk.core.sdk.EkycSdk
import com.luminsoft.ekyc_android_sdk.core.utils.ResourceProvider
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.view_model.NationalIdFrontOcrViewModel
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.ui_components.components.BackGroundView
import com.luminsoft.ekyc_android_sdk.ui_components.components.BottomSheet
import com.luminsoft.ekyc_android_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.ekyc_android_sdk.ui_components.components.DialogView
import com.luminsoft.ekyc_android_sdk.ui_components.components.LoadingView
import com.luminsoft.ekyc_android_sdk.ui_components.components.NormalTextField
import org.koin.compose.koinInject


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NationalIdOnBoardingFrontConfirmationScreen(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val document = onBoardingViewModel.nationalIdFrontImage.collectAsState()

    val nationalIdFrontOcrVM =
        document.value?.let { NationalIdFrontOcrViewModel(koinInject(), koinInject(), it) }

    val nationalIdFrontOcrViewModel = remember { nationalIdFrontOcrVM }

    val customerData = nationalIdFrontOcrViewModel!!.customerData.collectAsState()
    val loading = nationalIdFrontOcrViewModel.loading.collectAsState()
    val failure = nationalIdFrontOcrViewModel.failure.collectAsState()

    BackGroundView(navController = navController, showAppBar = true) {
        if (loading.value) LoadingView()
        else if (customerData.value != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                textItem(R.string.nameAr, customerData.value!!.fullName!!, R.drawable.user_icon)
                Spacer(modifier = Modifier.height(10.dp))
                textItem(R.string.nameEn, customerData.value!!.fullNameEn!!, R.drawable.user_icon)
                Spacer(modifier = Modifier.height(10.dp))
                textItem(R.string.address, customerData.value!!.address!!, R.drawable.address_icon)
                Spacer(modifier = Modifier.height(10.dp))
                textItem(
                    R.string.birthDate,
                    customerData.value!!.birthdate!!,
                    R.drawable.calendar_icon
                )
                Spacer(modifier = Modifier.height(10.dp))
                textItem(
                    R.string.nationalIdNumber,
                    customerData.value!!.idNumber!!,
                    R.drawable.id_card_icon
                )
                Spacer(modifier = Modifier.height(10.dp))
                textItem(
                    R.string.factoryNumber,
                    customerData.value!!.documentNumber!!,
                    R.drawable.factory_num_icon
                )
            }
        }
        else if (!failure.value?.message.isNullOrEmpty()) {
            if (failure.value is AuthFailure) {
                failure.value?.let {
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            activity.finish()
                            EkycSdk.ekycCallback?.error(PaymentFailedModel(it.message, it))

                        },
                    )
                    {
                        activity.finish()
                        EkycSdk.ekycCallback?.error(PaymentFailedModel(it.message, it))

                    }
                }
            } else {
                failure.value?.let {
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.retry),
                        onPressedButton = {
                            nationalIdFrontOcrViewModel.retry(navController)
                        },
                        secondButtonText = stringResource(id = R.string.exit),
                        onPressedSecondButton = {
                            activity.finish()
                            EkycSdk.ekycCallback?.error(PaymentFailedModel(it.message, it))

                        }
                    )
                    {
                        activity.finish()
                        EkycSdk.ekycCallback?.error(PaymentFailedModel(it.message, it))
                    }
                }
            }
        }
    }
}

@Composable
private fun textItem(label: Int, value: String, icon: Int) {
    NormalTextField(
        label = ResourceProvider.instance.getStringResource(label),
        value = TextFieldValue(text = value),
        onValueChange = { },
        enabled = false,
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
