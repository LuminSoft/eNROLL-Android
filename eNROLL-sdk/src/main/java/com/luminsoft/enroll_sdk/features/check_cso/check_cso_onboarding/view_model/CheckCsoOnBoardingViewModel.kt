package com.luminsoft.enroll_sdk.features.check_cso.check_cso_onboarding.view_model

import androidx.lifecycle.ViewModel
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_domain.usecases.CheckCsoUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class CheckCsoOnBoardingViewModel(
    private val checkCsoUseCase: CheckCsoUseCase
) : ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var csoChecked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var csoSucceeded: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    init {
        callCheckCso()
    }

    fun callCheckCso() {
        loading.value = true
        failure.value = null
        ui {
            val response = checkCsoUseCase.call(null)
            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    loading.value = false
                    csoChecked.value = true
                    csoSucceeded.value = s.status!!
                }
            )
        }
    }
}
