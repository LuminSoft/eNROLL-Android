package com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_onboarding.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_models.CheckNtraResponseModel
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_domain.usecases.CheckNtraUseCase
import kotlinx.coroutines.flow.MutableStateFlow


class CheckNtraOnBoardingViewModel(
    private val checkNtraUseCase: CheckNtraUseCase
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)

    var ntraChecked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var ntraSucceeded: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    init {
        callCheckNtra()
    }


    fun callCheckNtra() {
        checkNtra()
    }

    private fun checkNtra() {
        loading.value = true
        ui {

            val response: Either<SdkFailure, CheckNtraResponseModel> =
                checkNtraUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false

                },
                { s ->
                    s.let {
                        loading.value = false
                        ntraChecked.value = true
                        ntraSucceeded.value = s.status!!
                    }
                })
        }

    }

}
