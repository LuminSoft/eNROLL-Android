package com.luminsoft.enroll_sdk.features.check_aml.check_aml_onboarding.view_model

import CheckAmlUseCase
import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.check_aml.check_aml_data.check_aml_models.CheckAmlResponseModel
import kotlinx.coroutines.flow.MutableStateFlow


class CheckAmlOnBoardingViewModel(
    private val checkAmlUseCase: CheckAmlUseCase
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)

    var amlChecked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var amlSucceeded: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    init {
        callCheckAml()
    }


    fun callCheckAml() {
        checkAml()
    }

    private fun checkAml() {
        loading.value = true
        ui {

            val response: Either<SdkFailure, CheckAmlResponseModel> =
                checkAmlUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false

                },
                { s ->
                    s.let {
                        loading.value = false
                        amlChecked.value = true
                        amlSucceeded.value = s.isWhiteListed!!
                    }
                })
        }

    }

}


