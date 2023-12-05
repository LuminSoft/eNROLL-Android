package com.luminsoft.ekyc_android_sdk.main.main_onboarding.view_model

import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import kotlinx.coroutines.flow.MutableStateFlow

interface MainViewModel {
    var loading: MutableStateFlow<Boolean>
    var isButtonLoading: MutableStateFlow<Boolean>
    var failure: MutableStateFlow<com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure?>
    var params: MutableStateFlow<Any?>

    suspend fun retry(navController: NavController)
}