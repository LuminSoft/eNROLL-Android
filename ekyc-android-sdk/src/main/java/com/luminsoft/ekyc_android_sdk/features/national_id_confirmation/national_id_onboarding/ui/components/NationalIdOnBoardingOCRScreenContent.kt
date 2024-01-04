package com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.ui.components

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.view_model.NationalIdFrontOcrViewModel
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.ui_components.components.BackGroundView
import com.luminsoft.ekyc_android_sdk.ui_components.components.LoadingView
import com.luminsoft.ekyc_android_sdk.ui_components.components.NormalTextField
import org.koin.compose.koinInject

var tenantSecret = mutableStateOf(TextFieldValue(text = "test"))


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NationalIdOnBoardingFrontConfirmationScreen(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val document = onBoardingViewModel.nationalIdFrontImage.collectAsState()
    var nationalIdFrontOcrVM =
        document.value?.let { NationalIdFrontOcrViewModel(koinInject(), koinInject(), it) }
    var customerData = nationalIdFrontOcrVM?.customerData?.value
    var nationalIdFrontOcrViewModel = remember { nationalIdFrontOcrVM }


    BackGroundView(navController = navController, showAppBar = true) {
        NormalTextField(
            label = "Tenant Secret",
            value = tenantSecret.value,
            onValueChange = {
                tenantSecret.value = it
            })
//        if (nationalIdFrontOcrViewModel!!.loading.value) LoadingView()
//        Text(text = customerData!!.firstName!!)
//        nationalIdFrontOcrViewModel!!.customerData.value!!.firstName?.let { Text(text = it) }
//        nationalIdFrontOcrVM?.customerData.let { it!!.value!!.firstName?.let { it1 -> Text(text = it1) } }
    }
}
