package com.luminsoft.enroll_sdk.main.main_presentation.common

import androidx.navigation.NavController
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import kotlinx.coroutines.flow.MutableStateFlow

interface MainViewModel {
    var loading: MutableStateFlow<Boolean>
    var isButtonLoading: MutableStateFlow<Boolean>
    var failure: MutableStateFlow<SdkFailure?>
    var params: MutableStateFlow<Any?>
    var token: MutableStateFlow<String?>

    fun retry(navController: NavController)
}